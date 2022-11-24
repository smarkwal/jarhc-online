package org.jarhc.online.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S3Client {

	private static final Logger logger = LogManager.getLogger(S3Client.class);

	public S3Client(String region, String bucketName) {
		logger.debug("Region: {}", region);
		logger.debug("Bucket: {}", bucketName);
	}

	public boolean exists(String filePath) {
		logger.warn("TODO: exists({})", filePath);
		return false;
	}

	public String getURL(String filePath) {
		logger.warn("TODO: getURL({})", filePath);
		return filePath;
	}

}
