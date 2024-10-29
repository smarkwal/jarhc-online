package org.jarhc.online.jarhc;

import static org.jarhc.online.jarhc.Response.errorMessage;
import static org.jarhc.online.jarhc.Response.reportURL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import org.jarhc.online.Artifact;
import org.jarhc.online.JsonUtils;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.MavenException;
import org.jarhc.online.clients.S3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler implements RequestHandler<Request, Response> {

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	private static final Pattern reportFileNamePattern = Pattern.compile("^[a-z][A-Za-z0-9-.]*(\\.html|\\.txt)$");

	private final S3 s3;
	private final Maven maven;
	private final JarHC jarhc;

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

		// create JARHC wrapper
		jarhc = new JarHC();

		logger.debug("Handler initialized.");
	}

	// visible for testing
	Handler(S3 s3, Maven maven, JarHC jarhc) {
		this.s3 = s3;
		this.maven = maven;
		this.jarhc = jarhc;
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
				if (logger.isInfoEnabled()) {
					logger.info("Report file found in S3: {}", reportFileURL);
				}
				return reportURL(reportFileURL);
			}

			// create JarHC report
			List<String> classpath = request.getClasspath();
			List<String> provided = request.getProvided();
			File reportFile = new File("/tmp/report.html");
			jarhc.execute(classpath, provided, reportFile);

			// upload report to S3
			s3.upload(reportFile, reportFileName);

			return reportURL(reportFileURL);

		} catch (Exception e) {
			logger.error("Internal error", e);
			return errorMessage("Internal error: " + e);
		}

	}

	private Response validateRequest(Request request) throws MavenException {

		List<String> classpath = request.getClasspath();
		List<String> provided = request.getProvided();
		String reportFileName = request.getReportFileName();
		if (logger.isInfoEnabled()) {
			logger.info("Classpath: {}", classpath);
			logger.info("Provided: {}", provided);
			logger.info("Report file name: {}", reportFileName);
		}

		// validate classpath libraries
		if (classpath.isEmpty()) {
			return errorMessage("Classpath must not be empty.");
		} else if (classpath.size() > 10) {
			return errorMessage("Classpath must not contain more than 10 artifacts.");
		} else {
			Response response = validateLibraries(classpath);
			if (response != null) {
				return response;
			}
		}

		// validate provided libraries
		if (provided.isEmpty()) {
			// OK (provided is optional)
		} else if (provided.size() > 10) {
			return errorMessage("Provided must not contain more than 10 artifacts.");
		} else {
			Response response = validateLibraries(provided);
			if (response != null) {
				return response;
			}
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

	private Response validateLibraries(List<String> versions) throws MavenException {

		for (String version : versions) {

			// validate coordinates
			if (!Artifact.isValidVersion(version)) {
				return errorMessage("Artifact coordinates '" + version + "' are not valid.");
			}

			// check if artifact exists in Maven Central
			var artifact = new Artifact(version);
			boolean exists = maven.exists(artifact);
			if (!exists) {
				return errorMessage("Artifact '" + version + "' not found in Maven Central.");
			}
		}

		return null;
	}

}