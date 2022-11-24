package org.jarhc.online.rest.models;

public class MavenSearchError {

	private final String errorMessage;

	public MavenSearchError(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
