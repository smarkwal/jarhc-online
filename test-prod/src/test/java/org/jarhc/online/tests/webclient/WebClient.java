package org.jarhc.online.tests.webclient;

public class WebClient {

	public static WebRequest get(String url) {
		return request("GET", url);
	}

	public static WebRequest post(String url) {
		return request("POST", url);
	}

	public static WebRequest put(String url) {
		return request("PUT", url);
	}

	public static WebRequest delete(String url) {
		return request("DELETE", url);
	}

	public static WebRequest head(String url) {
		return request("HEAD", url);
	}

	public static WebRequest options(String url) {
		return request("OPTIONS", url);
	}

	public static WebRequest patch(String url) {
		return request("PATCH", url);
	}

	public static WebRequest request(String method, String url) {
		return new WebRequest(method, url);
	}

}
