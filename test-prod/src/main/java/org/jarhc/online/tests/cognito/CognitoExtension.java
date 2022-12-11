package org.jarhc.online.tests.cognito;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.RevokeTokenRequest;
import java.lang.reflect.Parameter;
import java.util.Base64;
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
public class CognitoExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

	private static final String AWS_REGION = "eu-central-1";

	private AWSCognitoIdentityProvider client;
	private String clientId = null;
	private String clientSecret = null;
	private CognitoTokens tokens = null;

	@Override
	public void beforeAll(ExtensionContext extensionContext) {

		clientId = getSystemProperty("jarhc.cognito.client.id");
		clientSecret = getSystemProperty("jarhc.cognito.client.secret");
		String username = getSystemProperty("jarhc.cognito.username");
		String password = getSystemProperty("jarhc.cognito.password");

		if (clientId == null || clientSecret == null || username == null || password == null) {
			return;
		}

		try {

			// create Cognito client
			client = AWSCognitoIdentityProviderClient.builder().withRegion(AWS_REGION).build();

			// calculate secret hash
			Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, clientSecret.getBytes());
			byte[] hash = mac.doFinal((username + clientId).getBytes());
			String secretHash = Base64.getEncoder().encodeToString(hash);

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

			tokens = new CognitoTokens(result.getIdToken(), result.getAccessToken(), result.getRefreshToken());

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

	private static String getSystemProperty(String name) {
		String value = System.getProperty(name);
		if (value == null) {
			String message = "WARN: System property '" + name + "' is not defined.";
			System.err.println(message);
		}
		return value;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return parameterType.equals(CognitoTokens.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		if (parameterType.equals(CognitoTokens.class)) {
			return tokens;
		} else {
			return null;
		}
	}

}
