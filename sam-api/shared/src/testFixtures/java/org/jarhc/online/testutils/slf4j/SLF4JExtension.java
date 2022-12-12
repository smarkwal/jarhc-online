package org.jarhc.online.testutils.slf4j;

import java.lang.reflect.Parameter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SLF4JExtension implements AfterEachCallback, ParameterResolver {

	@Override
	public void afterEach(ExtensionContext context) {
		SLF4JLogEvents.INSTANCE.clearEvents();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return parameterType.equals(SLF4JLogEvents.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return SLF4JLogEvents.INSTANCE;
	}

}
