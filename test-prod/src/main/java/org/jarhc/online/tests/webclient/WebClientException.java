package org.jarhc.online.tests.webclient;

public class WebClientException extends RuntimeException {

	public WebClientException(String message) {
		super(message);
	}

	public WebClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebClientException(Throwable cause) {
		super(cause);
	}

}
