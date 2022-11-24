package org.jarhc.online.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LambdaClient {

	private static final Logger logger = LogManager.getLogger(LambdaClient.class);

	public LambdaClient(String region) {
		logger.debug("Region: {}", region);
	}

	public void invokeAsync(String functionName, Object payload) {
		logger.warn("TODO: invokeAsync({},{})", functionName, payload);
	}

}
