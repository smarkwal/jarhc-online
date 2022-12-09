package org.jarhc.online.tests;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractWebTest {

	private WebResponseSoftAssertions softly;

	@BeforeEach
	public void prepare() {
		softly = new WebResponseSoftAssertions();
	}

	@AfterEach
	public void assertAll() {
		softly.assertAll();
	}

	public WebResponseAssert assertThat(WebResponse response) {
		return softly.assertThat(response);
	}

	public WebResponseSoftAssertions softly() {
		return softly;
	}

	public AbstractStringAssert<?> assertBody(WebResponse response) {
		String body = response.getBody();
		return Assertions.assertThat(body);
	}

	protected void assertSecurityHeaders(WebResponse response, boolean https) {

		assertThat(response)
				.hasHeader("X-XSS-Protection", "1; mode=block")
				.hasHeader("X-Frame-Options", "SAMEORIGIN")
				.hasHeader("Referrer-Policy", "strict-origin-when-cross-origin")
				.hasHeader("X-Content-Type-Options", "nosniff");

		if (https) {
			assertThat(response).hasHeader("Strict-Transport-Security", "max-age=31536000");
		} else {
			assertThat(response).hasNoHeader("Strict-Transport-Security");
		}
	}

}
