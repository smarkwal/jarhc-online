package org.jarhc.online.japicc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.japicc.clients.Maven;
import org.jarhc.online.japicc.clients.S3;
import org.jarhc.online.japicc.models.Artifact;
import org.jarhc.online.japicc.models.JapiccCheckRequest;

@SuppressWarnings("unused")
public class Handler implements RequestHandler<JapiccCheckRequest, Void> { // TODO: return String for better testing

	private static final Logger logger = LogManager.getLogger(Handler.class);

	private static final Pattern reportFileNamePattern = Pattern.compile("^[a-z][A-Za-z0-9-.]*(\\.html|\\.txt)$");

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
	public Void handleRequest(JapiccCheckRequest request, Context context) {

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
			return null;
		}

		// validate versions
		validateLibrary(newVersion);
		validateLibrary(oldVersion);

		// create JarHC report
		File reportFile = new File("/tmp/report.html");
		japicc(oldVersion, newVersion, reportFile);

		// upload report to S3
		try {
			s3.upload(reportFile, reportFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error uploading report file to S3 bucket.", e);
		}

		return null;
	}

	private void japicc(String oldVersion, String newVersion, File reportFile) {
		try (Subsegment xray = AWSXRay.beginSubsegment("Handler.japicc")) {

			// run JAPICC
			try {

				// TODO: execute JAPICC
				try (FileWriter writer = new FileWriter(reportFile)) {
					writer.write("Date: " + new Date());
				}

			} catch (Exception e) {
				xray.addException(e);
				throw new RuntimeException("Error running JAPICC.", e);
			}

		}
	}

	private void validateLibrary(String version) {

		// check if version exists in Maven Central
		boolean exists;
		try {
			var artifact = new Artifact(version);
			exists = maven.exists(artifact);
		} catch (Exception e) {
			throw new RuntimeException("Error testing if version exists in Maven Central: " + version, e);
		}

		if (!exists) {
			throw new IllegalArgumentException("Version not found in Maven Central: " + version);
		}
	}

}
