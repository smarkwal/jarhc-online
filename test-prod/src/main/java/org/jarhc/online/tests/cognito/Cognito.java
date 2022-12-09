package org.jarhc.online.tests.cognito;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.RevokeTokenRequest;
import com.amazonaws.util.Base64;
import java.lang.reflect.Parameter;
import java.util.Map;
import javax.crypto.Mac;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit 5 extension for Cognito authentication.
 */
public class Cognito implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

	private static final String AWS_REGION = "eu-central-1";

	private AWSCognitoIdentityProvider client;
	private String clientId = null;
	private String clientSecret = null;
	private Tokens tokens = null;

	@Override
	public void beforeAll(ExtensionContext extensionContext) {

		clientId = getEnvironmentVariable("COGNITO_CLIENT_ID");
		clientSecret = getEnvironmentVariable("COGNITO_CLIENT_SECRET");
		String username = getEnvironmentVariable("COGNITO_USERNAME");
		String password = getEnvironmentVariable("COGNITO_PASSWORD");

		if (clientId == null || clientSecret == null || username == null || password == null) {
			return;
		}

		try {

			// create Cognito client
			client = AWSCognitoIdentityProviderClient.builder().withRegion(AWS_REGION).build();

			// calculate secret hash
			Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, clientSecret.getBytes());
			byte[] hash = mac.doFinal((username + clientId).getBytes());
			String secretHash = Base64.encodeAsString(hash);

			// prepare authentication parameters
			Map<String, String> authParameters = Map.of(
					"USERNAME", username,
					"PASSWORD", password,
					"SECRET_HASH", secretHash
			);

			// get tokens
			InitiateAuthRequest request = new InitiateAuthRequest()
					.withAuthFlow("USER_PASSWORD_AUTH")
					.withClientId(clientId)
					.withAuthParameters(authParameters);
			InitiateAuthResult response = client.initiateAuth(request);
			AuthenticationResultType result = response.getAuthenticationResult();

			tokens = new Tokens(result.getIdToken(), result.getAccessToken(), result.getRefreshToken());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void afterAll(ExtensionContext extensionContext) {

		if (tokens == null) {
			return;
		}

		String refreshToken = tokens.getRefreshToken();
		RevokeTokenRequest request = new RevokeTokenRequest()
				.withClientId(clientId)
				.withClientSecret(clientSecret)
				.withToken(refreshToken);
		client.revokeToken(request);
	}

	private static String getEnvironmentVariable(String name) {
		String value = System.getenv(name);
		if (value == null) {
			String message = "WARN: Environment variable '" + name + "' is not defined.";
			System.err.println(message);
		}
		return value;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return parameterType.equals(Tokens.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		if (parameterType.equals(Tokens.class)) {
			return tokens;
		} else {
			return null;
		}
	}

	public static class Tokens {

		private final String idToken;
		private final String accessToken;
		private final String refreshToken;

		public Tokens(String idToken, String accessToken, String refreshToken) {
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

}
