package org.jarhc.online.rest.clients;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

public class S3 {

	private static final Logger logger = LogManager.getLogger(S3.class);

	private final String bucketName;
	private final S3Client client;
	private final String bucketUrl;

	public S3(String region, String bucketName, String bucketUrl) {
		this.bucketName = bucketName;
		this.bucketUrl = bucketUrl;
		this.client = S3Client.builder()
				.region(Region.of(region))
				.httpClientBuilder(UrlConnectionHttpClient.builder())
				.build();
	}

	public boolean exists(String reportFileName) {

		String key = "reports/" + reportFileName;
		HeadObjectRequest request = HeadObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		logger.debug("Request: {}", request);

		HeadObjectResponse response;
		try {
			response = client.headObject(request);
		} catch (NoSuchKeyException e) {
			logger.debug("Key not found: " + key);
			return false;
		}

		logger.debug("Response: {}", response);
		return true;
	}

	public String getURL(String reportFileName) {
		return bucketUrl + "/reports/" + URLEncoder.encode(reportFileName, StandardCharsets.UTF_8);
	}

}
