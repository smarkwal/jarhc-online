package org.jarhc.online.jarhc;

import static org.jarhc.artifacts.MavenRepository.MAVEN_CENTRAL_URL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.app.Application;
import org.jarhc.app.Options;
import org.jarhc.artifacts.ArtifactFinder;
import org.jarhc.artifacts.MavenArtifactFinder;
import org.jarhc.artifacts.MavenRepository;
import org.jarhc.artifacts.Repository;
import org.jarhc.online.jarhc.clients.Maven;
import org.jarhc.online.jarhc.clients.S3;
import org.jarhc.online.jarhc.models.Artifact;
import org.jarhc.online.jarhc.models.JarhcCheckRequest;
import org.jarhc.utils.JarHcException;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Handler implements RequestHandler<JarhcCheckRequest, String> {

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
	public String handleRequest(JarhcCheckRequest request, Context context) {

		if (logger.isTraceEnabled()) {
			logger.trace("Environment:\n{}", JsonUtils.toJSON(System.getenv()));
			logger.trace("Request:\n{}", JsonUtils.toJSON(request));
			logger.trace("Context:\n{}", JsonUtils.toJSON(context));
		}

		List<String> classpath = request.getClasspath();
		List<String> provided = request.getProvided();
		String reportFileName = request.getReportFileName();
		logger.info("Classpath: {}", classpath);
		logger.info("Provided: {}", provided);
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

		// validate classpath libraries
		if (classpath == null || classpath.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'classpath' must not be null or empty.");
		} else if (classpath.size() > 10) {
			throw new IllegalArgumentException("Parameter 'classpath' must not contain more than 10 values.");
		}
		validateLibraries(classpath);

		// validate provided libraries
		if (provided != null) {
			if (provided.size() > 10) {
				throw new IllegalArgumentException("Parameter 'provided' must not contain more than 10 values.");
			}
			validateLibraries(provided);
		} else {
			provided = List.of();
		}

		// create JarHC report
		File reportFile = new File("/tmp/report.html");
		jarHC(classpath, provided, reportFile);

		// upload report to S3
		try {
			s3.upload(reportFile, reportFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error uploading report file to S3 bucket.", e);
		}

		return "OK:" + reportFileURL;
	}

	private void jarHC(List<String> classpath, List<String> provided, File reportFile) {
		try (Subsegment xray = AWSXRay.beginSubsegment("Handler.jarHC")) {

			// prepare JarHC options
			Options options = new Options();
			options.setDataPath("/tmp/jarhc-data");
			for (String version : classpath) {
				options.addClasspathJarPath(version);
			}
			for (String version : provided) {
				options.addProvidedJarPath(version);
			}
			options.addReportFile(reportFile.getAbsolutePath());

			// prepare repository
			Repository repository = createRepository(options);

			// run JarHC
			try {
				org.slf4j.Logger appLogger = LoggerFactory.getLogger(Application.class);
				Application app = new Application(appLogger);
				app.setRepository(repository);
				int exitCode = app.run(options);
				if (exitCode != 0) {
					throw new RuntimeException("JarHC check failed with exit code " + exitCode + ".");
				}
			} catch (Exception e) {
				xray.addException(e);
				throw new RuntimeException("Error running JarHC.", e);
			}

		}
	}

	private Repository createRepository(Options options) {

		String dataPath = options.getDataPath();

		File directory = new File(dataPath);
		if (!directory.isDirectory()) {
			boolean created = directory.mkdirs();
			if (!created) {
				throw new JarHcException("Failed to create directory: " + directory.getAbsolutePath());
			}
		}

		File cacheDir = new File(dataPath, "checksums");
		org.slf4j.Logger mavenArtifactFinderLogger = LoggerFactory.getLogger(MavenArtifactFinder.class);
		ArtifactFinder artifactFinder = new MavenArtifactFinder(cacheDir, mavenArtifactFinderLogger);

		int javaVersion = options.getRelease();
		org.slf4j.Logger mavenRepositoryLogger = LoggerFactory.getLogger(MavenRepository.class);
		return new MavenRepository(javaVersion, MAVEN_CENTRAL_URL, dataPath, artifactFinder, mavenRepositoryLogger);
	}

	private void validateLibraries(List<String> versions) {

		for (String version : versions) {

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

}