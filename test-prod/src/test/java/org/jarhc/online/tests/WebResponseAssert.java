package org.jarhc.online.tests;

import static org.apache.http.HttpHeaders.CONTENT_LENGTH;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.jarhc.online.tests.ContentTypes.APPLICATION_JSON_UTF8;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;
import org.skyscreamer.jsonassert.JSONAssert;

public class WebResponseAssert extends AbstractAssert<WebResponseAssert, WebResponse> {

	public WebResponseAssert(WebResponse actual) {
		super(actual, WebResponseAssert.class);
	}

	public static WebResponseAssert assertThat(WebResponse actual) {
		return new WebResponseAssert(actual);
	}

	public WebResponseAssert hasStatusCode(int statusCode) {
		isNotNull();
		if (actual.getStatusCode() != statusCode) {
			failWithMessage("Expected response status code to be <%d> but was <%d>", statusCode, actual.getStatusCode());
		}
		return this;
	}

	public WebResponseAssert hasStatusCode(int... statusCodes) {
		isNotNull();
		int actualStatusCode = actual.getStatusCode();
		boolean found = Arrays.stream(statusCodes).anyMatch(s -> s == actualStatusCode);
		if (!found) {
			String codes = Arrays.stream(statusCodes).boxed().map(Object::toString).collect(Collectors.joining(","));
			failWithMessage("Expected response status code to be one of <%s> but was <%d>", codes, actualStatusCode);
		}
		return this;
	}

	public WebResponseAssert isOK() {
		return hasStatusCode(SC_OK);
	}

	public WebResponseAssert isNoContent() {
		return hasStatusCode(SC_NO_CONTENT);
	}

	public WebResponseAssert isMovedPermanently() {
		return hasStatusCode(SC_MOVED_PERMANENTLY);
	}

	public WebResponseAssert isNotFound() {
		return hasStatusCode(SC_NOT_FOUND);
	}

	public WebResponseAssert isBadRequest() {
		return hasStatusCode(SC_BAD_REQUEST);
	}

	public WebResponseAssert isMethodNotAllowed() {
		return hasStatusCode(SC_METHOD_NOT_ALLOWED);
	}

	public WebResponseAssert isUnauthorized() {
		return hasStatusCode(SC_UNAUTHORIZED);
	}

	public WebResponseAssert isForbidden() {
		return hasStatusCode(SC_FORBIDDEN);
	}

	public WebResponseAssert isServiceUnavailable() {
		return hasStatusCode(SC_SERVICE_UNAVAILABLE);
	}

	public WebResponseAssert isInternalServerError() {
		return hasStatusCode(SC_INTERNAL_SERVER_ERROR);
	}

	public WebResponseAssert hasHeader(String name) {
		isNotNull();
		if (!actual.hasHeader(name)) {
			failWithMessage("Expected response to contain header <%s>", name);
		}
		return this;
	}

	public WebResponseAssert hasNoHeader(String name) {
		isNotNull();
		if (actual.hasHeader(name)) {
			failWithMessage("Expected response to not contain header <%s>, but value was <%s>", name, actual.getHeader(name));
		}
		return this;
	}

	public WebResponseAssert hasHeader(String name, String value) {
		hasHeader(name);
		if (!actual.hasHeader(name, value)) {
			failWithMessage("Expected response to contain header <%s> with value <%s> but was <%s>", name, value, actual.getHeader(name));
		}
		return this;
	}

	public WebResponseAssert hasContentType(String contentType) {
		hasHeader(CONTENT_TYPE);
		if (actual.hasHeader(CONTENT_TYPE)) {
			String value = actual.getHeader(CONTENT_TYPE);
			if (!value.equals(contentType)) {
				failWithMessage("Expected response to have content type <%s> but was <%s>", contentType, value);
			}
		}
		return this;
	}

	public WebResponseAssert hasNoContentType() {
		hasNoHeader(CONTENT_TYPE);
		return this;
	}

	public WebResponseAssert hasContentTypeApplicationJsonUTF8() {
		hasContentType(APPLICATION_JSON_UTF8);
		return this;
	}

	public WebResponseAssert hasContentLength() {
		hasHeader(CONTENT_LENGTH);
		return this;
	}

	public WebResponseAssert hasNoContentLength() {
		hasNoHeader(CONTENT_LENGTH);
		return this;
	}

	public WebResponseAssert hasContentLength(long contentLength) {
		hasHeader(CONTENT_LENGTH, String.valueOf(contentLength));
		return this;
	}

	public WebResponseAssert hasContentLengthOf(String content) {
		// calculate expected content length using UTF-8 encoding
		int length = content.getBytes(StandardCharsets.UTF_8).length;
		return hasContentLength(length);
	}

	public WebResponseAssert hasNoBody() {
		isNotNull();
		if (actual.getBody() != null) {
			failWithMessage("Expected response to have no body");
		}
		return this;
	}

	public WebResponseAssert hasBody() {
		isNotNull();
		if (actual.getBody() == null) {
			failWithMessage("Expected response to have a body");
		}
		return this;
	}

	public WebResponseAssert hasEmptyBody() {
		isNotNull();
		hasBody();
		if (actual.getBody().length() > 0) {
			failWithMessage("Expected response to have empty body");
		}
		return this;
	}

	public WebResponseAssert hasBodyWithContent(String content) {
		hasBody();
		if (!actual.getBody().equals(content)) {
			failWithMessage("Expected response to have body with content <%s> but was <%s>", content, actual.getBody());
		}
		return this;
	}

	public WebResponseAssert hasJsonBody() {
		hasBody();
		if (!actual.hasJsonBody()) {
			failWithMessage("Expected response to have JSON body");
		}
		return this;
	}

	public WebResponseAssert hasJsonObject() {
		hasJsonBody();
		if (!actual.hasJsonObject()) {
			failWithMessage("Expected response to have JSON object in body");
		}
		return this;
	}

	public WebResponseAssert hasJsonObject(String json) {
		hasJsonObject();
		JSONAssert.assertEquals(json, actual.getBody(), true);
		return this;
	}

	public WebResponseAssert hasJsonArray() {
		hasJsonBody();
		if (!actual.hasJsonArray()) {
			failWithMessage("Expected response to have JSON array in body");
		}
		return this;
	}

	public WebResponseAssert hasJsonProperty(String name) {
		hasJsonObject();
		if (!actual.hasJsonProperty(name)) {
			failWithMessage("Expected response to contain JSON property <%s>", name);
		}
		return this;
	}

}
