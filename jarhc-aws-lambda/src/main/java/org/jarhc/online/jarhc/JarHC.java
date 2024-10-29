package org.jarhc.online.jarhc;

import static org.jarhc.artifacts.MavenRepository.MAVEN_CENTRAL_URL;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.File;
import java.util.List;
import org.jarhc.app.Application;
import org.jarhc.app.Options;
import org.jarhc.artifacts.ArtifactFinder;
import org.jarhc.artifacts.MavenArtifactFinder;
import org.jarhc.artifacts.MavenRepository;
import org.jarhc.artifacts.Repository;
import org.jarhc.utils.JarHcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JarHC {

	void execute(List<String> classpath, List<String> provided, File reportFile) {
		try (Subsegment xray = AWSXRay.beginSubsegment("JarHC.execute")) {

			int exitCode;
			try {

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
				Logger appLogger = LoggerFactory.getLogger(Application.class);
				Application app = new Application(appLogger);
				app.setRepository(repository);
				exitCode = app.run(options);

			} catch (Exception e) {
				xray.addException(e);
				throw new RuntimeException("Error running JarHC.", e);
			}

			// handle exit code
			if (exitCode != 0) {
				throw new RuntimeException("JarHC failed with exit code " + exitCode + ".");
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
		Logger mavenArtifactFinderLogger = LoggerFactory.getLogger(MavenArtifactFinder.class);
		ArtifactFinder artifactFinder = new MavenArtifactFinder(cacheDir, mavenArtifactFinderLogger);

		int javaVersion = options.getRelease();
		Logger mavenRepositoryLogger = LoggerFactory.getLogger(MavenRepository.class);
		return new MavenRepository(javaVersion, MAVEN_CENTRAL_URL, dataPath, artifactFinder, mavenRepositoryLogger);
	}

}
