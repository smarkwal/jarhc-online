package org.jarhc.online.rest.models;

public class JapiccCheckMessage {

	private final String oldVersion;
	private final String newVersion;
	private final String reportFileName;

	public JapiccCheckMessage(String oldVersion, String newVersion, String reportFileName) {
		this.oldVersion = oldVersion;
		this.newVersion = newVersion;
		this.reportFileName = reportFileName;
	}

	public String getOldVersion() {
		return oldVersion;
	}

	public String getNewVersion() {
		return newVersion;
	}

	public String getReportFileName() {
		return reportFileName;
	}

}
