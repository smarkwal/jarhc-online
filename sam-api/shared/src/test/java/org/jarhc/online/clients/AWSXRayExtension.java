package org.jarhc.online.clients;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Helper class for JUnit 5 tests to support AWS X-Ray.
 */
public class AWSXRayExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

	private Segment segment;

	@Override
	public void beforeEach(ExtensionContext context) {
		String name = context.getDisplayName();
		segment = AWSXRay.beginSegment(name);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		segment.close();
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) {
		AWSXRay.clearTraceEntity();
	}

}
