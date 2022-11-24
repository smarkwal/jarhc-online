package org.jarhc.online.rest.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lambda {

	private static final Logger logger = LogManager.getLogger(Lambda.class);

	public Lambda(String region) {
		logger.debug("Region: {}", region);
	}

	public void invokeAsync(String functionName, Object payload) {
		logger.warn("TODO: invokeAsync({},{})", functionName, payload);
	}

}
