package org.jarhc.online.testutils.slf4j;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

class MemoryLogger extends LegacyAbstractLogger {

	MemoryLogger(String name) {
		this.name = name;
	}

	@Override
	protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {

		// format message
		FormattingTuple tuple = MessageFormatter.arrayFormat(messagePattern, arguments, throwable);
		String message = tuple.getMessage();
		throwable = tuple.getThrowable();

		// write message to console
		String output = String.format("%-5s %s - %s", level, name, message);
		System.out.println(output);
		if (throwable != null) {
			throwable.printStackTrace(System.out);
		}

		// remember event in memory
		LogEvent event = new LogEvent(level, message, throwable);
		SLF4JLogEvents.INSTANCE.addEvent(event);
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	protected String getFullyQualifiedCallerName() {
		return null;
	}

}
