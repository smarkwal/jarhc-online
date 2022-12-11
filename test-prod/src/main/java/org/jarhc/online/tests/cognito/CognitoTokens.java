package org.jarhc.online.tests.cognito;

public class CognitoTokens {

	private final String idToken;
	private final String accessToken;
	private final String refreshToken;

	public CognitoTokens(String idToken, String accessToken, String refreshToken) {
		this.idToken = idToken;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getIdToken() {
		return idToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
