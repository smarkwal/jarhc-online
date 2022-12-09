package org.jarhc.online.tests.webclient;

import static org.apache.http.HttpHeaders.CONTENT_LENGTH;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.cookie.SM.COOKIE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class WebRequest {

	public static final String MULTIPART_BOUNDARY = "TestBoundary";

	private final String method;
	private final String url;
	private final Map<String, String> queryStringParams = new LinkedHashMap<>();
	private final Map<String, String> headers = new LinkedHashMap<>();
	private final Map<String, String> cookies = new LinkedHashMap<>();
	private String body;
	private MultipartEntityBuilder parts;
	private String characterEncoding;

	public WebRequest(String method, String url) {
		this.method = method;
		this.url = url;
	}

	public WebRequest addQueryStringParameter(String name, String value) {
		queryStringParams.put(name, value);
		return this;
	}

	public WebRequest addHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	public WebRequest addCookies(Map<String, String> cookies) {
		this.cookies.putAll(cookies);
		return this;
	}

	public WebRequest setContentLength(long contentLength) {
		headers.put(CONTENT_LENGTH, String.valueOf(contentLength));
		return this;
	}

	public WebRequest setContentType(String contentType) {
		headers.put(CONTENT_TYPE, contentType);
		return this;
	}

	public WebRequest setBasicCredentials(String username, String password) {
		headers.put("Authorization", "Basic " + encodeBase64(username + ":" + password));
		return this;
	}

	private String encodeBase64(String value) {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		return encoder.encodeToString(bytes);
	}

	public WebRequest setBody(String body) {
		return setBody(body, StandardCharsets.UTF_8.name());
	}

	public WebRequest setBody(String body, String characterEncoding) {
		if (parts != null) throw new IllegalArgumentException("parts already added");
		this.body = body;
		this.characterEncoding = characterEncoding;
		return this;
	}

	public WebRequest addMultipartFile(String name, String fileName, String contentType, String content) {
		initMultipartEntityBuilder();
		String encoding = ContentTypes.getCharset(contentType);
		if (encoding == null) encoding = Encodings.UTF_8;
		byte[] data;
		try {
			data = content.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		parts.addBinaryBody(name, data, ContentType.parse(contentType), fileName);
		return this;
	}

	public WebRequest addMultipartFormField(String name, String value) {
		initMultipartEntityBuilder();
		parts.addTextBody(name, value);
		return this;
	}

	public WebRequest setMultipartCharset(String charset) {
		initMultipartEntityBuilder();
		parts.setCharset(charset != null ? Charset.forName(charset) : null);
		return this;
	}

	private void initMultipartEntityBuilder() {
		if (body != null) throw new IllegalArgumentException("body already set");
		if (parts == null) {
			parts = MultipartEntityBuilder.create();
			parts.setCharset(StandardCharsets.UTF_8);
			parts.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			parts.setBoundary(MULTIPART_BOUNDARY);
		}
	}

	public WebResponse execute() {

		URI uri = buildURI();
		logRequest(uri);

		try {
			try (CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build()) {
				HttpRequestBase method = buildHttpRequest(uri);
				addHeaders(method);
				addBody(method);
				log("------------------------------------------------------");
				try (CloseableHttpResponse response = httpClient.execute(method)) {
					return buildWebResponse(response);
				}
			}
		} catch (IOException e) {
			throw new WebClientException(e);
		}
	}

	private URI buildURI() {

		StringBuilder url = new StringBuilder(this.url);

		boolean first = true;
		for (Map.Entry<String, String> parameter : queryStringParams.entrySet()) {
			if (first) {
				url.append("?");
				first = false;
			} else {
				url.append("&");
			}
			url.append(parameter.getKey()); // TODO: URL-encode
			String value = parameter.getValue();
			if (value != null) {
				url.append("=").append(value); // TODO: URL-encode
			}
		}

		try {
			return new URI(url.toString());
		} catch (URISyntaxException e) {
			throw new WebClientException(e);
		}

	}

	private HttpRequestBase buildHttpRequest(URI uri) {
		switch (method) {
			case "GET":
				return new HttpGet(uri);
			case "POST":
				return new HttpPost(uri);
			case "PUT":
				return new HttpPut(uri);
			case "DELETE":
				return new HttpDelete(uri);
			case "HEAD":
				return new HttpHead(uri);
			case "PATCH":
				return new HttpPatch(uri);
			case "OPTIONS":
				return new HttpOptions(uri);
			default:
				throw new WebClientException("Unsupported method: " + method);
		}
	}

	private void addHeaders(HttpRequestBase method) {
		for (Map.Entry<String, String> header : headers.entrySet()) {
			String name = header.getKey();
			String value = header.getValue();
			if (name.equals("Authorization") && value.startsWith("Bearer ")) {
				log("> " + name + ": " + value.substring(0, 20) + "...");
			} else {
				log("> " + name + ": " + value);
			}
			method.addHeader(name, value);
		}
		for (Map.Entry<String, String> cookie : cookies.entrySet()) {
			String header = String.format("%s=%s", cookie.getKey(), cookie.getValue());
			log("> Cookie: " + header);
			method.addHeader(COOKIE, header);
		}
	}

	private void addBody(HttpRequestBase method) {
		if (body == null && parts == null) return;
		if (method instanceof HttpEntityEnclosingRequestBase) {
			HttpEntityEnclosingRequestBase bodyMethod = (HttpEntityEnclosingRequestBase) method;
			if (body != null) {
				log(">");
				log("> " + body);
				bodyMethod.setEntity(new StringEntity(body, characterEncoding));
			} else { // parts != null
				HttpEntity multipart = parts.build();
				bodyMethod.setHeader(multipart.getContentType()); // override Content-Type for multipart boundary
				bodyMethod.setEntity(multipart);
				log("> " + multipart.getContentType());
				log(">");
				log("> [multipart]"); // todo: list parts
			}
		} else {
			throw new UnsupportedOperationException(method.getMethod() + " cannot have a body");
		}
	}

	private WebResponse buildWebResponse(CloseableHttpResponse response) throws IOException {
		logHeaders(response);
		HttpEntity entity = response.getEntity();
		String body = null;
		if (entity != null) {
			body = EntityUtils.toString(entity);
			logBody(response, body);
		}
		log("======================================================");
		return new WebResponse(response, body);
	}

	private void logRequest(URI uri) {
		log("======================================================");
		log("> " + method + " " + uri);
	}

	private void logHeaders(HttpResponse response) {
		StatusLine statusLine = response.getStatusLine();
		log("< " + statusLine);
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			log("< " + header.getName() + ": " + header.getValue());
		}
	}

	private void logBody(HttpResponse response, String body) {
		log("<");
		if (response.containsHeader(HttpHeaders.CONTENT_TYPE)) {
			String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
			if (ContentTypes.isText(contentType)) {
				log("< " + body);
				return;
			}
		}
		if (response.containsHeader(CONTENT_LENGTH)) {
			log("< [" + response.getFirstHeader(CONTENT_LENGTH).getValue() + " bytes]");
		} else {
			log("< [unknown data]");
		}
	}

	private static void log(String message) {
		System.out.println(message);
	}

}
