package org.jarhc.online.rest.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.rest.JsonUtils;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class Lambda {

	private static final Logger logger = LogManager.getLogger(Lambda.class);

	private final LambdaClient client;

	public Lambda(String region) {
		this.client = LambdaClient.builder()
				.region(Region.of(region))
				.httpClientBuilder(UrlConnectionHttpClient.builder())
				.build();
	}

	public void invokeAsync(String functionName, Object payload) throws Exception {

		// serialize payload
		String json = JsonUtils.toJSON(payload);

		// prepare async event request
		InvokeRequest request = InvokeRequest.builder()
				.functionName(functionName)
				.invocationType(InvocationType.EVENT) // async
				.payload(SdkBytes.fromUtf8String(json))
				.build();
		logger.debug("Request: {}", request);

		// invoke function
		InvokeResponse response;
		try {
			response = client.invoke(request);
		} catch (Exception e) {
			logger.error("Error:", e);
			throw e;
		}

		logger.debug("Response: {}", response);
	}

}
