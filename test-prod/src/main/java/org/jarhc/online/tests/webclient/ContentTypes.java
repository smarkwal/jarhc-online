package org.jarhc.online.tests.webclient;

public final class ContentTypes {

	private ContentTypes() {
	}

	public static final String TEXT_PREFIX = "text/";
	public static final String TEXT_PLAIN = TEXT_PREFIX + "plain";
	public static final String TEXT_PLAIN_UTF8 = getContentTypeWithCharset(TEXT_PLAIN, Encodings.UTF_8);
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_JSON_UTF8 = getContentTypeWithCharset(APPLICATION_JSON, Encodings.UTF_8);
	public static final String APPLICATION_XML = "application/xml";
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	public static String getApplicationJsonWithCharset(String charset) {
		return getContentTypeWithCharset(APPLICATION_JSON, charset);
	}

	public static String getContentTypeWithCharset(String contentType, String charset) {
		return String.format("%s;charset=%s", contentType, charset);
	}

	public static String getCharset(String contentType) {
		int pos = contentType.indexOf("charset=");
		if (pos < 0) return null;
		return contentType.substring(pos + 8).trim();
	}

	public static boolean isText(String contentType) {
		return contentType.startsWith(TEXT_PREFIX) || contentType.startsWith(APPLICATION_JSON) || contentType.startsWith(APPLICATION_XML);
	}

}
