package org.jarhc.online.clients;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import org.jarhc.online.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class Lambda {

	private static final Logger logger = LoggerFactory.getLogger(Lambda.class);

	private final LambdaClient client;

	public Lambda() {
		this.client = LambdaClient.builder()
				.httpClientBuilder(UrlConnectionHttpClient.builder())
				.build();
	}

	public void invokeAsync(String functionName, Object payload) throws LambdaException {
		try (Subsegment xray = AWSXRay.beginSubsegment("Lambda.invokeAsync")) {
			xray.putAnnotation("functionName", functionName);

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
				xray.addException(e);
				throw new LambdaException("Lambda error", e);
			}

			logger.debug("Response: {}", response);
		}
	}

}
