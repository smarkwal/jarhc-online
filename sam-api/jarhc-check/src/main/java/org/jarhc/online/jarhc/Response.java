package org.jarhc.online.jarhc;

public class Response {

	private final String reportURL;
	private final String errorMessage;

	public static Response reportURL(String reportURL) {
		return new Response(reportURL, null);
	}

	public static Response errorMessage(String errorMessage) {
		return new Response(null, errorMessage);
	}

	private Response(String reportURL, String errorMessage) {
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
