package org.jarhc.online.rest.models;

public class JapiccCheckRequest {

	private String oldVersion;
	private String newVersion;

	public JapiccCheckRequest() {
	}

	public String getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}

	public String getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}

}
