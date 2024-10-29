package org.jarhc.online.clients;

public class S3Exception extends Exception {

	public S3Exception(String message) {
		super(message);
	}

	public S3Exception(String message, Throwable cause) {
		super(message, cause);
	}

}
