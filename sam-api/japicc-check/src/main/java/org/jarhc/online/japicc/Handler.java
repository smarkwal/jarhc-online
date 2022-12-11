package org.jarhc.online.japicc;

import static org.jarhc.online.japicc.Response.errorMessage;
import static org.jarhc.online.japicc.Response.reportURL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.File;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.Artifact;
import org.jarhc.online.JsonUtils;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.MavenException;
import org.jarhc.online.clients.S3;

public class Handler implements RequestHandler<Request, Response> {

	private static final Logger logger = LogManager.getLogger();

	private static final Pattern reportFileNamePattern = Pattern.compile("^[a-z][A-Za-z0-9-.]*(\\.html|\\.txt)$");

	private final S3 s3;
	private final Maven maven;
	private final JAPICC japicc;

	public Handler() {
		logger.debug("Initializing Handler ...");

		var bucketName = System.getenv("BUCKET_NAME");
		if (bucketName == null) {
			bucketName = "localhost";
		}

		var bucketUrl = System.getenv("BUCKET_URL");
		if (bucketUrl == null) {
			bucketUrl = "http://localhost:3000";
		}

		// create AWS clients
		s3 = new S3(bucketName, bucketUrl);
		maven = new Maven(10 * 1000); // 10 seconds

		// create JAPICC wrapper
		japicc = new JAPICC();

		logger.debug("Handler initialized.");
	}

	// visible for testing
	Handler(S3 s3, Maven maven, JAPICC japicc, Logger logger) {
		this.s3 = s3;
		this.maven = maven;
		this.japicc = japicc;
	}

	@Override
	public Response handleRequest(Request request, Context context) {

		if (logger.isTraceEnabled()) {
			logger.trace("Environment:\n{}", JsonUtils.toJSON(System.getenv()));
			logger.trace("Request:\n{}", JsonUtils.toJSON(request));
			logger.trace("Context:\n{}", JsonUtils.toJSON(context));
		}

		try {

			// validate request
			Response response = validateRequest(request);
			if (response != null) {
				return response;
			}

			// check if report file already exists in S3
			String reportFileName = request.getReportFileName();
			boolean exists = s3.exists(reportFileName);
			var reportFileURL = s3.getURL(reportFileName);
			if (exists) {
				logger.info("Report file found in S3: {}", reportFileURL);
				return reportURL(reportFileURL);
			}

			// download old artifact from Maven Central
			String oldVersion = request.getOldVersion();
			File oldVersionFile = downloadArtifact(oldVersion);
			if (oldVersionFile == null) {
				return errorMessage("Artifact '" + oldVersion + "' not found in Maven Central.");
			}

			// download new artifact from Maven Central
			String newVersion = request.getNewVersion();
			File newVersionFile = downloadArtifact(newVersion);
			if (newVersionFile == null) {
				return errorMessage("Artifact '" + newVersion + "' not found in Maven Central.");
			}

			// create JAPICC report
			File reportFile = new File("/tmp/report.html");
			japicc.execute(oldVersionFile, newVersionFile, reportFile);

			// upload report to S3
			s3.upload(reportFile, reportFileName);

			return reportURL(reportFileURL);

		} catch (Exception e) {
			logger.error("Internal error", e);
			return errorMessage("Internal error: " + e);
		}

	}

	private Response validateRequest(Request request) {

		String oldVersion = request.getOldVersion();
		String newVersion = request.getNewVersion();
		String reportFileName = request.getReportFileName();
		logger.info("Old version: {}", oldVersion);
		logger.info("New version: {}", newVersion);
		logger.info("Report file name: {}", reportFileName);

		// validate old version
		if (!Artifact.isValidVersion(oldVersion)) {
			return errorMessage("Artifact coordinates '" + oldVersion + "' are not valid.");
		}

		// validate new version
		if (!Artifact.isValidVersion(newVersion)) {
			return errorMessage("Artifact coordinates '" + newVersion + "' are not valid.");
		}

		// validate report file name
		if (reportFileName == null || reportFileName.isEmpty()) {
			return errorMessage("Report file name must not be null or empty.");
		} else if (!reportFileNamePattern.matcher(reportFileName).matches()) {
			return errorMessage("Report file name is not valid.");
		}

		// request is valid
		return null;
	}

	private File downloadArtifact(String version) throws MavenException {
		Artifact artifact = new Artifact(version);
		File file = new File("/tmp", artifact.toFilePath());
		if (!file.isFile()) {
			boolean download = maven.download(artifact, file);
			if (!download) {
				return null;
			}
		}
		return file;
	}

}
