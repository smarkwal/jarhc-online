package org.jarhc.online.tests;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.jarhc.online.tests.cognito.Cognito;
import org.jarhc.online.tests.webclient.AbstractWebTest;
import org.jarhc.online.tests.webclient.WebClient;
import org.jarhc.online.tests.webclient.WebResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

class ApiTest extends AbstractWebTest {

	static final String ORIGIN = "https://online.jarhc.org";

	@Nested
	@DisplayName("/maven/search")
	class MavenSearch {

		String url = "https://api.jarhc.org/maven/search";

		@Test
		@DisplayName("OPTIONS (preflight request)")
		void options() {
			test_preflightRequest(url);
		}

		@Test
		@DisplayName("POST")
		void post() {

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"coordinates\":\"commons-io:commons-io:2.10.0\"}")
					.execute();

			// assert
			assertThat(response)
					.isOK()
					.hasContentType("application/json")
					.hasContentLength(167)
					.hasJsonObject("{\"coordinates\":\"commons-io:commons-io:2.10.0\",\"artifacts\":[{\"groupId\":\"commons-io\",\"artifactId\":\"commons-io\",\"version\":\"2.10.0\"}]}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, true);
		}

		@Test
		@DisplayName("POST (unknown artifact)")
		void post_withUnknownArtifact() {

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"coordinates\":\"unknown:unknown:1.0.0\"}")
					.execute();

			// assert
			assertThat(response)
					.isOK()
					.hasContentType("application/json")
					.hasContentLength(66)
					.hasJsonObject("{\"coordinates\":\"unknown:unknown:1.0.0\",\"artifacts\":[]}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, true);
		}

		@Test
		@DisplayName("POST (invalid artifact) -> 400 Bad Request")
		void post_witInvalidArtifact() {

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"coordinates\":\"invalid\"}")
					.execute();

			// assert
			assertThat(response)
					.isBadRequest()
					.hasContentType("application/json")
					.hasContentLength(100)
					.hasJsonObject("{\"errorMessage\":\"java.lang.IllegalArgumentException: Invalid artifact coordinates: 'invalid'\"}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, true);
		}

	}

	@Nested
	@DisplayName("/japicc/submit")
	@ExtendWith(Cognito.class)
	class JapiccSubmit {

		String url = "https://api.jarhc.org/japicc/submit";

		@Test
		@DisplayName("OPTIONS (preflight request)")
		void options() {
			// test
			test_preflightRequest(url);
		}

		@Test
		@DisplayName("POST")
		void post(Cognito.Tokens tokens) {
			assumeTrue(tokens != null, "Cognito tokens are required for this test.");

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Authorization", "Bearer " + tokens.getIdToken())
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"oldVersion\":\"commons-io:commons-io:2.10.0\",\"newVersion\":\"commons-io:commons-io:2.11.0\"}")
					.execute();

			// assert
			assertThat(response)
					.isOK()
					.hasContentType("application/json")
					.hasContentLength(131)
					.hasJsonObject("{\"reportURL\":\"https://online.jarhc.org/reports/report-5d75918f818d3baf3565a9ac506b4eb4d08b2e7341f979d1bc99c06ab8e2ab1d.html\"}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, true);
		}

		@Test
		@DisplayName("POST (unauthenticated) -> 401 Unauthorized")
		void post_unauthenticated() {

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"oldVersion\":\"commons-io:commons-io:2.10.0\",\"newVersion\":\"commons-io:commons-io:2.11.0\"}")
					.execute();

			// assert
			assertThat(response)
					.isUnauthorized()
					.hasHeader("x-amzn-ErrorType", "UnauthorizedException")
					.hasContentType("application/json")
					.hasContentLength(26)
					.hasJsonObject("{\"message\":\"Unauthorized\"}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, false);
		}

	}

	@Nested
	@DisplayName("/japicc/submit")
	@ExtendWith(Cognito.class)
	class JarhcSubmit {

		String url = "https://api.jarhc.org/jarhc/submit";

		@Test
		@DisplayName("OPTIONS (preflight request)")
		void options_jarhcSubmit() {
			// test
			test_preflightRequest(url);
		}

		@Test
		@DisplayName("POST")
		void post(Cognito.Tokens tokens) {
			assumeTrue(tokens != null, "Cognito tokens are required for this test.");

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Authorization", "Bearer " + tokens.getIdToken())
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"classpath\":[\"commons-io:commons-io:2.11.0\"],\"provided\":[]}")
					.execute();

			// assert
			assertThat(response)
					.isOK()
					.hasContentType("application/json")
					.hasContentLength(131)
					.hasJsonObject("{\"reportURL\":\"https://online.jarhc.org/reports/report-cbbcae8ff7d5ad70f56a1f5abfa2e705dc2eb50ad6eb574cbd66d983a2b2731d.html\"}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, true);
		}

		@Test
		@DisplayName("POST (unauthenticated) -> 401 Unauthorized")
		void post_unauthenticated() {

			// test
			WebResponse response = WebClient.post(url)
					.addHeader("Origin", ORIGIN)
					.addHeader("Content-Type", "application/json")
					.setBody("{\"classpath\":[\"commons-io:commons-io:2.11.0\"],\"provided\":[]}")
					.execute();

			// assert
			assertThat(response)
					.isUnauthorized()
					.hasHeader("x-amzn-ErrorType", "UnauthorizedException")
					.hasContentType("application/json")
					.hasContentLength(26)
					.hasJsonObject("{\"message\":\"Unauthorized\"}");

			assertCorsResponseHeaders(response);
			assertApiGatewayHeaders(response, false);
		}

	}

	// helper methods ----------------------------------------------------------

	private void test_preflightRequest(String url) {

		// test
		WebResponse response = WebClient.options(url)
				.addHeader("Origin", ORIGIN)
				.addHeader("Access-Control-Request-Headers", "Content-Type")
				.addHeader("Access-Control-Request-Method", "POST")
				.execute();

		// assert
		assertThat(response)
				.isOK()
				.hasContentType("application/json")
				.hasContentLength(3)
				.hasBodyWithContent("{}\n");

		assertCorsResponseHeaders(response);
		assertThat(response)
				.hasHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS") // TODO: why is GET allowed?
				.hasHeader("Access-Control-Allow-Headers", "Content-Type,Authorization")
				.hasHeader("Access-Control-Max-Age", "600");

		assertApiGatewayHeaders(response, false);
	}

	private void assertCorsResponseHeaders(WebResponse response) {
		assertThat(response)
				.hasHeader("Access-Control-Allow-Origin", ORIGIN)
				.hasHeader("Access-Control-Allow-Credentials", "true");
	}

	private void assertApiGatewayHeaders(WebResponse response, boolean traced) {
		assertThat(response)
				.hasHeader("x-amzn-RequestId")
				.hasHeader("x-amz-apigw-id");

		if (traced) {
			assertThat(response).hasHeader("X-Amzn-Trace-Id");
		} else {
			assertThat(response).hasNoHeader("X-Amzn-Trace-Id");
		}
	}

}
