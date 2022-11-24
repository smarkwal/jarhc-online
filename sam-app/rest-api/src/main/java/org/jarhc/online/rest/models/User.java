package org.jarhc.online.rest.models;

public class User {

	private final String subject;
	private final String email;
	private final String sourceIP;
	private final String userAgent;

	public User(String subject, String email, String sourceIP, String userAgent) {
		this.subject = subject;
		this.email = email;
		this.sourceIP = sourceIP;
		this.userAgent = userAgent;
	}

	public String getSubject() {
		return subject;
	}

	public String getEmail() {
		return email;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public String getUserAgent() {
		return userAgent;
	}

	@Override
	public String toString() {
		return String.format("User{subject=%s,email=%s,sourceIP=%s,userAgent=%s}", subject, email, sourceIP, userAgent);
	}

}
