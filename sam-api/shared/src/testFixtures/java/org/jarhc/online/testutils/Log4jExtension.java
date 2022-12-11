package org.jarhc.online.testutils;

import java.lang.reflect.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class Log4jExtension implements BeforeAllCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

	private final Log4jMemoryAppender appender = new Log4jMemoryAppender();

	@Override
	public void beforeAll(ExtensionContext context) {

		// start appender
		appender.start();

		// add appender to root logger
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Configuration configuration = loggerContext.getConfiguration();
		LoggerConfig rootLogger = configuration.getRootLogger();
		rootLogger.addAppender(appender, null, null);
		loggerContext.updateLoggers();
	}

	@Override
	public void afterEach(ExtensionContext context) {
		appender.clear();
	}

	@Override
	public void afterAll(ExtensionContext context) {

		// remove appender from root logger
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Configuration configuration = loggerContext.getConfiguration();
		LoggerConfig rootLogger = configuration.getRootLogger();
		rootLogger.removeAppender(appender.getName());
		loggerContext.updateLoggers();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return parameterType.equals(Log4jMemoryAppender.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return appender;
	}

}
