package org.jarhc.online.japicc;

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
import org.jarhc.online.Artifact;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.MavenException;
import org.jarhc.online.clients.S3;
import org.jarhc.online.clients.S3Exception;
import org.jarhc.online.testutils.Log4jExtension;
import org.jarhc.online.testutils.Log4jMemoryAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(Log4jExtension.class)
class HandlerTest {

	private static final String BUCKET_URL = "https://online.jarhc.org/reports/";
	private static final String OLD_VERSION = "commons-io:commons-io:2.10.0";
	private static final String NEW_VERSION = "commons-io:commons-io:2.11.0";
	private static final String REPORT_FILE_NAME = "report-1234567890.html";

	private static final Artifact OLD_VERSION_ARTIFACT = new Artifact(OLD_VERSION);
	private static final Artifact NEW_VERSION_ARTIFACT = new Artifact(NEW_VERSION);

	private static final File OLD_VERSION_FILE = new File("/tmp/commons-io/commons-io/2.10.0/commons-io-2.10.0.jar");
	private static final File NEW_VERSION_FILE = new File("/tmp/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar");
	private static final File REPORT_FILE = new File("/tmp/report.html");

	@Mock
	Context context;

	@Mock
	Maven maven;

	@Mock
	S3 s3;

	@Mock
	JAPICC japicc;

	@InjectMocks
	Handler handler;

	Request request = new Request();

	Log4jMemoryAppender appender;

	@BeforeEach
	void setUp(Log4jMemoryAppender appender) {
		this.appender = appender;

		request.setOldVersion(OLD_VERSION);
		request.setNewVersion(NEW_VERSION);
		request.setReportFileName(REPORT_FILE_NAME);
	}

	@AfterEach
	void tearDown() {
		appender.assertNoEvents();
		verifyNoMoreInteractions(maven, s3, japicc);
	}

	@Test
	void constructor() {

		// test
		new Handler();

		// assert
		appender.assertDebug("Initializing Handler ...");
		appender.assertDebug("Handler initialized.");
	}

	@Test
	void handleRequest() throws MavenException, S3Exception {

		// setup
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doReturn(true).when(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);
		doReturn(true).when(maven).download(NEW_VERSION_ARTIFACT, NEW_VERSION_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		assertReportURL(response);

		// verify
		verify(japicc).execute(OLD_VERSION_FILE, NEW_VERSION_FILE, REPORT_FILE);
		verify(s3).upload(REPORT_FILE, REPORT_FILE_NAME);
	}

	@Test
	void handleRequest_returnsReportURL_whenReportAlreadyExistsInS3() throws S3Exception {

		// setup
		doReturn(true).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		appender.assertInfo("Report file found in S3: https://online.jarhc.org/reports/report-1234567890.html");
		assertReportURL(response);
	}

	@Test
	void handleRequest_returnsErrorMessage_whenOldVersionIsInvalid() {

		// prepare
		request.setOldVersion("abc:xyz");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: abc:xyz");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact coordinates 'abc:xyz' are not valid.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenNewVersionIsInvalid() {

		// prepare
		request.setNewVersion("xyz:abc");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: xyz:abc");
		appender.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact coordinates 'xyz:abc' are not valid.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsNull() {

		// prepare
		request.setReportFileName(null);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: null");
		assertErrorMessage(response, "Report file name must not be null or empty.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsEmpty() {

		// prepare
		request.setReportFileName("");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: ");
		assertErrorMessage(response, "Report file name must not be null or empty.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenReportFileNameIsInvalid() {

		// prepare
		request.setReportFileName("output/result.json");

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: output/result.json");
		assertErrorMessage(response, "Report file name is not valid.");
	}

	@Test
	void handleRequest_returnsErrorMessage_whenOldVersionIsNotFoundInMavenCentral() throws MavenException, S3Exception {

		// setup
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doReturn(false).when(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact 'commons-io:commons-io:2.10.0' not found in Maven Central.");

		// verify
		verify(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);
	}

	@Test
	void handleRequest_returnsErrorMessage_whenNewVersionIsNotFoundInMavenCentral() throws MavenException, S3Exception {

		// setup
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doReturn(true).when(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);
		doReturn(false).when(maven).download(NEW_VERSION_ARTIFACT, NEW_VERSION_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		assertErrorMessage(response, "Artifact 'commons-io:commons-io:2.11.0' not found in Maven Central.");

		// verify
		verify(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);
		verify(maven).download(NEW_VERSION_ARTIFACT, NEW_VERSION_FILE);
	}

	@Test
	void handleRequest_returnsInternalError_onS3Exception() throws S3Exception {

		// setup
		doThrow(new S3Exception("S3 error")).when(s3).exists(REPORT_FILE_NAME);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		appender.assertError("Internal error");
		assertErrorMessage(response, "Internal error: org.jarhc.online.clients.S3Exception: S3 error");
	}

	@Test
	void handleRequest_returnsInternalError_onMavenException() throws MavenException, S3Exception {

		// setup
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doThrow(new MavenException("Maven error")).when(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		appender.assertError("Internal error");
		assertErrorMessage(response, "Internal error: org.jarhc.online.clients.MavenException: Maven error");
	}

	@Test
	void handleRequest_returnsInternalError_onJAPICCException() throws MavenException, S3Exception {

		// setup
		doReturn(false).when(s3).exists(REPORT_FILE_NAME);
		doAnswer(args -> BUCKET_URL + args.getArgument(0)).when(s3).getURL(REPORT_FILE_NAME);
		doReturn(true).when(maven).download(OLD_VERSION_ARTIFACT, OLD_VERSION_FILE);
		doReturn(true).when(maven).download(NEW_VERSION_ARTIFACT, NEW_VERSION_FILE);
		doThrow(new RuntimeException("JAPICC error")).when(japicc).execute(OLD_VERSION_FILE, NEW_VERSION_FILE, REPORT_FILE);

		// test
		Response response = handler.handleRequest(request, context);

		// assert
		appender.assertInfo("Old version: commons-io:commons-io:2.10.0");
		appender.assertInfo("New version: commons-io:commons-io:2.11.0");
		appender.assertInfo("Report file name: report-1234567890.html");
		appender.assertError("Internal error");
		assertErrorMessage(response, "Internal error: java.lang.RuntimeException: JAPICC error");
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
