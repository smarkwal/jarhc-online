package org.jarhc.online.tests.webclient;

import org.assertj.core.api.SoftAssertions;

public class WebResponseSoftAssertions extends SoftAssertions {

	public WebResponseAssert assertThat(WebResponse actual) {
		return proxy(WebResponseAssert.class, WebResponse.class, actual);
	}

}
