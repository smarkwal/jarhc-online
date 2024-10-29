package org.jarhc.online.clients;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3 {

	private static final Logger logger = LoggerFactory.getLogger(S3.class);

	private final String bucketName;
	private final String bucketUrl;
	private final S3Client client;

	public S3(String bucketName, String bucketUrl) {
		this.bucketName = bucketName;
		this.bucketUrl = bucketUrl;
		this.client = S3Client.builder()
				.httpClientBuilder(UrlConnectionHttpClient.builder())
				.build();
	}

	public boolean exists(String reportFileName) throws S3Exception {
		try (Subsegment xray = AWSXRay.beginSubsegment("S3.exists")) {
			xray.putAnnotation("reportFileName", reportFileName);

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
				xray.putAnnotation("found", false);
				return false;
			} catch (Exception e) {
				xray.addException(e);
				throw new S3Exception("S3 error", e);
			}

			logger.debug("Response: {}", response);
			xray.putAnnotation("found", true);
			return true;
		}
	}

	public String getURL(String reportFileName) {
		return bucketUrl + "/reports/" + URLEncoder.encode(reportFileName, StandardCharsets.UTF_8);
	}

	public void upload(File reportFile, String reportFileName) throws S3Exception {
		try (Subsegment xray = AWSXRay.beginSubsegment("S3.upload")) {
			xray.putAnnotation("reportFileName", reportFileName);

			String key = "reports/" + reportFileName;
			PutObjectRequest request = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(key)
					.contentType("text/html")
					.cacheControl("no-cache")
					.build();
			logger.debug("Request: {}", request);

			PutObjectResponse response;
			try {
				response = client.putObject(request, reportFile.toPath());
			} catch (Exception e) {
				xray.addException(e);
				throw new S3Exception("S3 upload error", e);
			}

			logger.debug("Response: {}", response);
		}
	}

}
