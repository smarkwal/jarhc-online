package org.jarhc.online.clients;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.Artifact;

public class Maven {

	private static final Logger logger = LogManager.getLogger();

	private static final String BASE_URL = "https://repo1.maven.org/maven2";
	private static final int STATUS_OK = 200;
	private static final int STATUS_NOT_FOUND = 404;

	private static final Map<String, Boolean> CACHE = new HashMap<>();

	private final int timeout;

	/**
	 * Create a Maven client.
	 *
	 * @param timeout Timeout in milliseconds.
	 */
	public Maven(int timeout) {
		this.timeout = timeout;
	}

	public boolean exists(Artifact artifact) throws MavenException {
		try (Subsegment xray = AWSXRay.beginSubsegment("Maven.exists")) {
			var coordinates = artifact.toCoordinates();
			xray.putAnnotation("artifact", coordinates);

			// check if artifact result exists in cache
			if (CACHE.containsKey(coordinates)) {
				boolean found = CACHE.get(coordinates);
				xray.putAnnotation("cached", found);
				if (found) {
					logger.debug("Artifact found : {} (cached)", coordinates);
					return true;
				} else {
					logger.debug("Artifact not found : {} (cached)", coordinates);
					return false;
				}
			}

			// send HTTP HEAD request to Maven Central repository URL
			int statusCode = sendRequest("HEAD", artifact, null, xray);

			// interpret response status code
			switch (statusCode) {
				case STATUS_OK:
					logger.debug("Artifact found : {}", coordinates);
					CACHE.put(coordinates, true);
					return true;
				case STATUS_NOT_FOUND:
					logger.debug("Artifact not found : {}", coordinates);
					CACHE.put(coordinates, false);
					return false;
				default:
					throw new MavenException("Unexpected status code: " + statusCode);
			}
		}
	}

	public boolean download(Artifact artifact, File file) throws MavenException {
		try (Subsegment xray = AWSXRay.beginSubsegment("Maven.download")) {
			var coordinates = artifact.toCoordinates();
			xray.putAnnotation("artifact", coordinates);

			// send HTTP GET request to Maven Central repository URL
			Consumer<HttpURLConnection> processor = connection -> {
				try {
					saveResponseToFile(connection, file);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			};
			int statusCode = sendRequest("GET", artifact, processor, xray);

			// interpret response status code
			switch (statusCode) {
				case STATUS_OK:
					logger.debug("Artifact downloaded : {} -> {}", coordinates, file.getAbsolutePath());
					return true;
				case STATUS_NOT_FOUND:
					logger.warn("Artifact not found : {}", coordinates);
					return false;
				default:
					throw new MavenException("Unexpected status code: " + statusCode);
			}
		}
	}

	private int sendRequest(String method, Artifact artifact, Consumer<HttpURLConnection> processor, Subsegment xray) throws MavenException {

		HttpURLConnection connection = null;
		try {

			// prepare URL
			var url = new URL(BASE_URL + artifact.toURLPath());
			logger.debug("Artifact URL: {}", url);

			// prepare HTTP connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setDoOutput(false);
			connection.setDoInput(true);

			// send request and get response
			connection.connect();

			// get response status code
			int statusCode = connection.getResponseCode();
			logger.debug("Status code: {}", statusCode);
			xray.putAnnotation("statusCode", statusCode);

			if (statusCode == STATUS_OK && processor != null) {
				processor.accept(connection);
			}

			return statusCode;

		} catch (Exception e) {
			xray.addException(e);
			throw new MavenException("HTTP error", e);
		} finally {
			// close connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private static void saveResponseToFile(HttpURLConnection connection, File file) throws IOException {

		// make sure that parent directory exists
		File directory = file.getParentFile();
		if (!directory.isDirectory()) {
			boolean created = directory.mkdirs();
			if (!created) {
				throw new IOException("Error creating directory: " + directory.getAbsolutePath());
			}
		}

		// save response body to file
		try (InputStream inputStream = connection.getInputStream()) {
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
				inputStream.transferTo(outputStream);
			}
		}
	}

}
