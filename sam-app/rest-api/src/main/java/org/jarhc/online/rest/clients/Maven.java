package org.jarhc.online.rest.clients;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.rest.models.Artifact;

public class Maven {

	private static final Logger logger = LogManager.getLogger(Maven.class);

	private static final String BASE_URL = "https://repo1.maven.org/maven2";

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

	public boolean exists(Artifact artifact) throws Exception {

		// check if artifact result exists in cache
		final var cacheKey = artifact.toCoordinates();
		if (CACHE.containsKey(cacheKey)) {
			boolean found = CACHE.get(cacheKey);
			if (found) {
				logger.debug("Artifact found : {} (cached)", cacheKey);
				return true;
			} else {
				logger.debug("Artifact not found : {} (cached)", cacheKey);
				return false;
			}
		}

		// send HTTP HEAD request to Maven Central repository URL
		HttpURLConnection connection = null;
		int statusCode;
		try {

			// prepare URL
			var url = new URL(BASE_URL + artifact.toURLPath());
			logger.debug("Artifact URL: {}", url);

			// prepare HTTP connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setDoOutput(false);
			connection.setDoInput(true);

			// send request and get response
			connection.connect();

			// get response status code
			statusCode = connection.getResponseCode();
			logger.debug("Status code: {}", statusCode);

		} catch (Exception e) {
			logger.error("Error:", e);
			throw e;
		} finally {
			// close connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		// interpret response status code
		switch (statusCode) {
			case 200:
				logger.debug("Artifact found : {}", cacheKey);
				CACHE.put(cacheKey, true);
				return true;
			case 404:
				logger.debug("Artifact not found : {}", cacheKey);
				CACHE.put(cacheKey, false);
				return false;
			default:
				logger.error("Unexpected status code: {}", statusCode);
				throw new RuntimeException("Unexpected status code: " + statusCode);
		}
	}

}
