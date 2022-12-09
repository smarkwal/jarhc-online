package org.jarhc.online.tests;

import org.jarhc.online.tests.webclient.AbstractWebTest;
import org.jarhc.online.tests.webclient.WebClient;
import org.jarhc.online.tests.webclient.WebResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WebsiteTest extends AbstractWebTest {

	@Test
	@DisplayName("HTTP -> HTTPS")
	void redirectToHttps() {

		// prepare
		@SuppressWarnings("HttpUrlsUsage")
		String url = "http://online.jarhc.org";

		// test
		WebResponse response = WebClient.get(url).execute();

		// assert
		assertThat(response)
				.isMovedPermanently()
				.hasHeader("Location", "https://online.jarhc.org/")
				.hasHeader("Server", "CloudFront")
				.hasContentType("text/html")
				.hasBody();

		assertSecurityHeaders(response, false);

		assertBody(response)
				.contains("<head><title>301 Moved Permanently</title></head>");
	}

	@Test
	@DisplayName("GET /")
	void root() {

		// prepare
		String url = "https://online.jarhc.org";

		// test
		WebResponse response = WebClient.get(url).execute();

		// assert
		assertThat(response)
				.isOK()
				.hasHeader("Server", "AmazonS3")
				.hasContentType("text/html")
				.hasBody();

		assertSecurityHeaders(response, true);

		assertBody(response)
				.startsWith("<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"/><link rel=\"icon\" href=\"favicon.ico\"/><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/>");
	}

	@Test
	@DisplayName("GET /favicon.ico")
	void favicon() {

		// prepare
		String url = "https://online.jarhc.org/favicon.ico";

		// test
		WebResponse response = WebClient.get(url).execute();

		// assert
		assertThat(response)
				.isOK()
				.hasHeader("Server", "AmazonS3")
				.hasContentType("image/x-icon")
				.hasContentLength(32988)
				.hasBody();

		assertSecurityHeaders(response, true);
	}

	@Test
	@DisplayName("GET /img/logo.png")
	void logo() {

		// prepare
		String url = "https://online.jarhc.org/img/logo.png";

		// test
		WebResponse response = WebClient.get(url).execute();

		// assert
		assertThat(response)
				.isOK()
				.hasHeader("Server", "AmazonS3")
				.hasContentType("image/png")
				.hasContentLength(29410)
				.hasBody();

		assertSecurityHeaders(response, true);
	}

	@Test
	@DisplayName("GET /reports/ -> 404 Not Found")
	void reports() {

		// prepare
		String url = "https://online.jarhc.org/reports/";

		// test
		WebResponse response = WebClient.get(url).execute();

		// assert
		assertThat(response)
				.isNotFound()
				.hasHeader("Server", "AmazonS3")
				.hasContentType("application/xml")
				.hasBody();

		assertSecurityHeaders(response, true);

		assertBody(response)
				.contains("<Error><Code>NoSuchKey</Code><Message>The specified key does not exist.</Message><Key>reports/</Key>");
	}

}
