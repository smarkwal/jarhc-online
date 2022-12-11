package org.jarhc.online.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

class LambdaTest {

	static LambdaClient client;

	@BeforeAll
	static void beforeAll() {
		client = LambdaClient.builder()
				.build();
	}

	@AfterAll
	static void afterAll() {
		client.close();
		// TODO: delete test reports in S3 bucket
	}

	@Test
	@Disabled("Not implemented")
	void restApi() {
		// TODO: implement tests:
		//  - POST /maven/search
		//  - POST /japicc/submit
		//  - POST /jarhc/submit
	}

	@Test
	void japiccCheck() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"oldVersion\":\"commons-io:commons-io:2.10.0\",\"newVersion\":\"commons-io:commons-io:2.11.0\",\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("japicc-check", payload, false);

		// assert
		assertSyncResponse(response, reportFileName);
	}

	@Test
	void japiccCheck_invokeAsync() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"oldVersion\":\"commons-io:commons-io:2.10.0\",\"newVersion\":\"commons-io:commons-io:2.11.0\",\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("japicc-check", payload, true);

		// assert
		assertAsyncResponse(response);
	}

	@Test
	void japiccCheck_unknownArtifact() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"oldVersion\":\"unknown:unknown:1.0.0\",\"newVersion\":\"unknown:unknown:2.0.0\",\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("japicc-check", payload, false);

		// assert
		assertErrorResponse(response, "Artifact 'unknown:unknown:1.0.0' not found in Maven Central.");
	}

	// TODO: implement tests for invalid JSON, invalid coordinates, and missing properties

	@Test
	void jarhcCheck() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"classpath\":[\"commons-io:commons-io:2.11.0\"],\"provided\":[],\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("jarhc-check", payload, false);

		// assert
		assertSyncResponse(response, reportFileName);
	}

	@Test
	void jarhcCheck_invokeAsync() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"classpath\":[\"commons-io:commons-io:2.11.0\"],\"provided\":[],\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("jarhc-check", payload, true);

		// assert
		assertAsyncResponse(response);
	}

	@Test
	void jarhcCheck_unknownArtifact() {

		// prepare
		String reportFileName = "test-report-" + System.currentTimeMillis() + ".html";
		String payload = "{\"classpath\":[\"unknown:unknown:1.0.0\"],\"provided\":[],\"reportFileName\":\"" + reportFileName + "\"}";

		// test
		InvokeResponse response = testFunction("jarhc-check", payload, false);

		// assert
		assertErrorResponse(response, "Artifact 'unknown:unknown:1.0.0' not found in Maven Central.");
	}

	// TODO: implement tests for invalid JSON, invalid coordinates, and missing properties

	// helper methods ----------------------------------------------------------

	private static InvokeResponse testFunction(String functionName, String payload, boolean async) {

		// prepare
		InvokeRequest request = InvokeRequest.builder()
				.functionName(functionName)
				.invocationType(async ? InvocationType.EVENT : InvocationType.REQUEST_RESPONSE)
				.payload(SdkBytes.fromUtf8String(payload))
				.build();

		// test
		return client.invoke(request);
	}

	private static void assertSyncResponse(InvokeResponse response, String reportFileName) {
		assertNotNull(response);
		assertEquals(200, response.statusCode());
		assertNotNull(response.payload());
		assertEquals("{\"reportURL\":\"https://online.jarhc.org/reports/" + reportFileName + "\"}", response.payload().asUtf8String());
		assertNull(response.functionError());
		assertNull(response.logResult());
	}

	private static void assertErrorResponse(InvokeResponse response, String errorMessage) {
		assertNotNull(response);
		assertEquals(200, response.statusCode());
		assertNotNull(response.payload());
		assertEquals("{\"errorMessage\":\"" + errorMessage + "\"}", response.payload().asUtf8String());
		assertNull(response.functionError());
		assertNull(response.logResult());
	}

	private static void assertAsyncResponse(InvokeResponse response) {
		assertNotNull(response);
		assertEquals(202, response.statusCode());
		assertNotNull(response.payload());
		assertEquals("", response.payload().asUtf8String());
		assertNull(response.functionError());
		assertNull(response.logResult());
	}

}
