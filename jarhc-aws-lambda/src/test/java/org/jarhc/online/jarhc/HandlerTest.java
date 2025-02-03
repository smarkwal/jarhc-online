package org.jarhc.online.jarhc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.amazonaws.services.lambda.runtime.Context;
import java.io.File;
import java.util.List;
import org.jarhc.online.Artifact;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.MavenException;
import org.jarhc.online.clients.S3;
import org.jarhc.online.clients.S3Exception;
import org.jarhc.online.testutils.slf4j.SLF4JExtension;
import org.jarhc.online.testutils.slf4j.SLF4JLogEvents;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SLF4JExtension.class)
class HandlerTest {

	private static final String BUCKET_URL = "https://online.jarhc.org/reports/";
	private static final String VERSION = "commons-io:commons-io:2.11.0";
	private static final String REPORT_FILE_NAME = "report-1234567890.html";

	private static final Artifact VERSION_ARTIFACT = new Artifact(VERSION);

	private static final File REPORT_FILE = new File("/tmp/report.html");

	@Mock
	Context context;

	@Mock
	Maven maven;

	@Mock
	S3 s3;

	@Mock
	JarHC jarhc;

	@InjectMocks
	Handler handler;

	Request request = new Request();

	SLF4JLogEvents logEvents;

	@BeforeEach
	void setUp(SLF4JLogEvents logEvents) {
		this.logEvents = logEvents;

		request.setClasspath(List.of(VERSION));
		request.setProvided(List.of());
		request.setReportFileName(REPORT_FILE_NAME);
	}

	@AfterEach
	void tearDown() {
		logEvents.assertNoEvents();
		verifyNoMoreInteractions(maven, s3, jarhc);
	}

	@Test
	void constructor() {

		// test
		new Handler();

		// assert
		logEvents.assertDebug("Initializing Handler ...");
		logEvents.assertDebug("Handler initialized.");
	}

	@Test
	void handleRequest() throws MavenException, S3Exception {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertReportURL(response);

		// verify
		verify(jarhc).execute(List.of(VERSION), List.of(), REPORT_FILE);
		verify(s3).upload(REPORT_FILE, REPORT_FILE_NAME);
	}

	@Test
	void handleRequest_returnsReportURL_whenReportAlreadyExistsInS3() throws MavenException, S3Exception {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);
		doReturn(true).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		logEvents.assertInfo("Report file found in S3: https://online.jarhc.org/reports/report-1234567890.html");
		assertReportURL(response);
	}

	@Test
	void handleRequest_returnsErrorMessage_whenClasspathIsEmpty() {

		// prepare
		request.setClasspath(List.of());

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: []");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Classpath must not be empty.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenClasspathIsToLong() {

		// prepare
		request.setClasspath(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Classpath must not contain more than 10 artifacts.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenClasspathIsInvalid() {

		// prepare
		request.setClasspath(List.of("abc:xyz"));

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [abc:xyz]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact coordinates 'abc:xyz' are not valid.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenClasspathIsNotFoundInMavenCentral() throws MavenException {

		// setup
		doReturn(false).when(maven).exists(VERSION_ARTIFACT);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact 'commons-io:commons-io:2.11.0' not found in Maven Central.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenProvidedIsToLong() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);

		// prepare
		request.setProvided(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Provided must not contain more than 10 artifacts.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenProvidedIsInvalid() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);

		// prepare
		request.setProvided(List.of("xyz:abc"));

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: [xyz:abc]");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact coordinates 'xyz:abc' are not valid.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenProvidedIsNotFoundInMavenCentral() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);
		doReturn(false).when(maven).exists(new Artifact("unknown:unknown:1.0.0"));

		// prepare
		request.setProvided(List.of("unknown:unknown:1.0.0"));

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: [unknown:unknown:1.0.0]");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact 'unknown:unknown:1.0.0' not found in Maven Central.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsNull() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);

		// prepare
		request.setReportFileName(null);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: null");
		assertErrorMessage(response, "Report file name must not be null or empty.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsEmpty() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);

		// prepare
		request.setReportFileName("");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: ");
		assertErrorMessage(response, "Report file name must not be null or empty.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsInvalid() throws MavenException {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);

		// prepare
		request.setReportFileName("output/result.json");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: output/result.json");
		assertErrorMessage(response, "Report file name is not valid.");
	}

	@Test
	void handleRequest_returnsInternalError_onMavenException() throws MavenException {

		// setup
		doThrow(new MavenException("Maven error")).when(maven).exists(VERSION_ARTIFACT);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		logEvents.assertError("Internal error");
		assertErrorMessage(response, "Internal error: org.jarhc.online.clients.MavenException: Maven error");
	}

	@Test
	void handleRequest_returnsInternalError_onS3Exception() throws MavenException, S3Exception {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);
		doThrow(new S3Exception("S3 error")).when(s3).exists(REPORT_FILE_NAME);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		logEvents.assertError("Internal error");
		assertErrorMessage(response, "Internal error: org.jarhc.online.clients.S3Exception: S3 error");
	}

	@Test
	void handleRequest_returnsInternalError_onJarHCException() throws MavenException, S3Exception {

		// setup
		doReturn(true).when(maven).exists(VERSION_ARTIFACT);
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doThrow(new RuntimeException("JarHC error")).when(jarhc).execute(List.of(VERSION), List.of(), REPORT_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		logEvents.assertInfo("Classpath: [commons-io:commons-io:2.11.0]");
		logEvents.assertInfo("Provided: []");
		logEvents.assertInfo("Report file name: report-1234567890.html");
		logEvents.assertError("Internal error");
		assertErrorMessage(response, "Internal error: java.lang.RuntimeException: JarHC error");
	}

	// helper methods ----------------------------------------------------------

	private static void assertReportURL(Response response) {
		assertNotNull(response);
		assertNull(response.getErrorMessage());
		assertEquals(BUCKET_URL + REPORT_FILE_NAME, response.getReportURL());
	}

	private static void assertErrorMessage(Response response, String errorMessage) {
		assertNotNull(response);
		assertNull(response.getReportURL());
		assertEquals(errorMessage, response.getErrorMessage());
	}

}
