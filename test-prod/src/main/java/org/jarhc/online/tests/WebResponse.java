package org.jarhc.online.tests;


import static org.apache.http.HttpHeaders.SERVER;
import static org.apache.http.cookie.SM.SET_COOKIE;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebResponse {

	private final HttpResponse response;
	private final String body;
	private final Object json;

	public static final int ERROR_CODE_DEFAULT = 10000;

	WebResponse(HttpResponse response, String body) {
		this.response = response;
		this.body = body;
		Object json = null;
		if (body != null) {
			try {
				json = new JSONObject(body);
			} catch (JSONException e1) {
				try {
					json = new JSONArray(body);
				} catch (JSONException e2) {
					// ignore?
				}
			}
		}
		this.json = json;
	}

	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

	public boolean hasStatusCode(int statusCode) {
		return getStatusCode() == statusCode;
	}

	public boolean hasHeader(String name) {
		return response.containsHeader(name);
	}

	public boolean hasHeader(String name, String value) {
		return response.containsHeader(name) && response.getFirstHeader(name).getValue().equals(value);
	}

	public String getHeader(String name) {
		if (response.containsHeader(name)) {
			return response.getFirstHeader(name).getValue();
		} else {
			return null;
		}
	}

	public List<String> getHeaders(String name) {
		if (response.containsHeader(name)) {
			Header[] headers = response.getHeaders(name);
			return Arrays.stream(headers).map(NameValuePair::getValue).collect(Collectors.toList());
		} else {
			return null;
		}
	}

	public Map<String, String> getCookies() {
		Map<String, String> cookies = new LinkedHashMap<>();
		List<String> headers = getHeaders(SET_COOKIE);
		for (String cookie : headers) {
			int start = cookie.indexOf("=");
			if (start < 0) continue;
			int end = cookie.indexOf(";", start);
			if (end < start) end = cookie.length();
			String name = cookie.substring(0, start);
			String value = cookie.substring(start + 1, end);
			cookies.put(name, value);
		}
		return cookies;
	}

	public boolean isServer(String server) {
		if (!hasHeader(SERVER)) return false;
		return getHeader(SERVER).contains(server);
	}

	public String getBody() {
		return body;
	}

	// JSON

	public boolean hasJsonBody() {
		return json != null;
	}

	public boolean hasJsonObject() {
		return json instanceof JSONObject;
	}

	public JSONObject getJsonObject() {
		return (JSONObject) json;
	}

	public boolean hasJsonArray() {
		return json instanceof JSONArray;
	}

	public JSONArray getJsonArray() {
		return (JSONArray) json;
	}

	public boolean hasJsonProperty(String name) {
		if (json instanceof JSONObject) {
			return ((JSONObject) json).has(name);
		} else {
			return false;
		}
	}

	public int getJsonErrorCode() {
		if (json instanceof JSONObject) {
			return ((JSONObject) json).getInt("errorCode");
		} else {
			return -1;
		}
	}

	public String getJsonMessage() {
		if (json instanceof JSONObject) {
			return ((JSONObject) json).getString("message");
		} else {
			return null;
		}
	}

}
