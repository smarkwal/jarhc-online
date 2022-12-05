package org.jarhc.online.japicc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.Artifact;
import org.jarhc.online.JsonUtils;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.S3;

@SuppressWarnings("unused")
public class Handler implements RequestHandler<JapiccCheckRequest, String> {

	private static final Logger logger = LogManager.getLogger(Handler.class);

	private static final Pattern reportFileNamePattern = Pattern.compile("^[a-z][A-Za-z0-9-.]*(\\.html|\\.txt)$");
	private static final String JAPI_COMPLIANCE_CHECKER = "/usr/bin/japi-compliance-checker";

	private final S3 s3;
	private final Maven maven;

	public Handler() {
		logger.debug("Initializing Handler ...");

		var region = System.getenv("AWS_REGION");
		if (region == null) {
			region = "eu-central-1";
		}

		var bucketName = System.getenv("BUCKET_NAME");
		if (bucketName == null) {
			bucketName = "localhost";
		}

		var bucketUrl = System.getenv("BUCKET_URL");
		if (bucketUrl == null) {
			bucketUrl = "http://localhost:3000";
		}

		// create AWS clients
		s3 = new S3(region, bucketName, bucketUrl);
		maven = new Maven(10 * 1000); // 10 seconds

		logger.debug("Handler initialized.");
	}

	@Override
	public String handleRequest(JapiccCheckRequest request, Context context) {

		if (logger.isTraceEnabled()) {
			logger.trace("Environment:\n{}", JsonUtils.toJSON(System.getenv()));
			logger.trace("Request:\n{}", JsonUtils.toJSON(request));
			logger.trace("Context:\n{}", JsonUtils.toJSON(context));
		}

		String oldVersion = request.getOldVersion();
		String newVersion = request.getNewVersion();
		String reportFileName = request.getReportFileName();
		logger.info("Old version: {}", oldVersion);
		logger.info("New version: {}", newVersion);
		logger.info("Report file name: {}", reportFileName);

		// validate report file name
		if (reportFileName == null || reportFileName.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'reportFileName' must not be null or empty.");
		} else if (!reportFileNamePattern.matcher(reportFileName).matches()) {
			throw new IllegalArgumentException("Parameter 'reportFileName' is not valid.");
		}

		// TODO: add parameter to skip S3 check

		// check if report file already exists in S3
		boolean exists;
		try {
			exists = s3.exists(reportFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error testing if report file exists in S3 bucket.", e);
		}

		var reportFileURL = s3.getURL(reportFileName);
		if (exists) {
			logger.info("Report file found in S3: {}", reportFileURL);
			return "EXISTS:" + reportFileURL;
		}

		// download artifacts from Maven Central
		File newVersionFile = downloadLibrary(newVersion);
		File oldVersionFile = downloadLibrary(oldVersion);

		// create JAPICC report
		File reportFile = new File("/tmp/report.html");
		japicc(newVersionFile, oldVersionFile, reportFile);

		// upload report to S3
		try {
			s3.upload(reportFile, reportFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error uploading report file to S3 bucket.", e);
		}

		return "OK:" + reportFileURL;
	}

	private void japicc(File oldVersionFile, File newVersionFile, File reportFile) {
		try (Subsegment xray = AWSXRay.beginSubsegment("Handler.japicc")) {

			// run JAPICC
			int exitCode;
			try {

				Process process = new ProcessBuilder()
						.command(
								JAPI_COMPLIANCE_CHECKER,
								"-old", oldVersionFile.getAbsolutePath(),
								"-new", newVersionFile.getAbsolutePath(),
								"-report-path", reportFile.getAbsolutePath()
						)
						//.redirectErrorStream(true)
						.start();

				exitCode = process.waitFor();

				try (InputStream inputStream = process.getInputStream()) {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					inputStream.transferTo(buffer);
					String output = buffer.toString();
					logger.info("JAPICC output:\n{}", output);
				}
				try (InputStream errorStream = process.getErrorStream()) {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					errorStream.transferTo(buffer);
					String output = buffer.toString();
					if (!output.isEmpty()) {
						logger.error("JAPICC error:\n{}", output);
					}
				}

			} catch (Exception e) {
				xray.addException(e);
				throw new RuntimeException("Error running JAPICC.", e);
			}

			// handle exit code
			if (exitCode != 0) {
				if (exitCode == 1) {
					// incompatibilities found
				} else {
					throw new RuntimeException("JAPICC failed with exit code " + exitCode);
				}
			}

		}
	}

	private File downloadLibrary(String version) {
		try {
			Artifact artifact = new Artifact(version);
			File file = new File("/tmp", artifact.toFilePath());
			if (!file.isFile()) {
				boolean download = maven.download(artifact, file);
				if (!download) {
					throw new RuntimeException("Artifact not found in Maven Central: " + artifact);
				}
			}
			return file;
		} catch (Exception e) {
			throw new RuntimeException("Error downloading version from Maven Central: " + version, e);
		}
	}

}
