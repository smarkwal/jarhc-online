package org.jarhc.online.rest;

public class CheckResponse {

	private final String reportURL;
	private final String errorMessage;

	public CheckResponse(String reportURL, String errorMessage) {
		this.reportURL = reportURL;
		this.errorMessage = errorMessage;
	}

	public String getReportURL() {
		return reportURL;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
