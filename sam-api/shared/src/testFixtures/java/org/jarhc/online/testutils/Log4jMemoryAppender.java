package org.jarhc.online.testutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public class Log4jMemoryAppender extends AbstractAppender {

	private final List<LogEvent> events = new ArrayList<>();

	public Log4jMemoryAppender() {
		super("Memory", null, null, false, null);
	}

	@Override
	public void append(LogEvent event) {
		events.add(event.toImmutable());
	}

	public List<LogEvent> getEvents() {
		return events;
	}

	public void clear() {
		events.clear();
	}

	public Log4jMemoryAppender assertTrace(String message) {
		return assertEvent(Level.TRACE, message);
	}

	public Log4jMemoryAppender assertDebug(String message) {
		return assertEvent(Level.DEBUG, message);
	}

	public Log4jMemoryAppender assertInfo(String message) {
		return assertEvent(Level.INFO, message);
	}

	public Log4jMemoryAppender assertWarn(String message) {
		return assertEvent(Level.WARN, message);
	}

	public Log4jMemoryAppender assertError(String message) {
		return assertEvent(Level.ERROR, message);
	}

	public Log4jMemoryAppender assertFatal(String message) {
		return assertEvent(Level.FATAL, message);
	}

	public Log4jMemoryAppender assertEvent(Level level, String message) {
		for (Iterator<LogEvent> iterator = events.iterator(); iterator.hasNext(); ) {
			LogEvent event = iterator.next();
			if (event.getLevel().equals(level) && event.getMessage().getFormattedMessage().equals(message)) {
				iterator.remove();
				return this;
			}
		}
		throw new AssertionError("No " + level.name() + " event found with message: " + message);
	}

	public Log4jMemoryAppender assertNoEvents() {
		if (events.isEmpty()) {
			return this;
		}
		StringBuilder message = new StringBuilder("Unexpected log events:\n");
		for (LogEvent event : events) {
			message.append("- ");
			message.append(event.getLevel().name());
			message.append(": ");
			message.append(event.getMessage().getFormattedMessage());
			message.append("\n");
		}

		message.append("Missing assertions:\n");
		for (LogEvent event : events) {
			String method = "assert" + event.getLevel().name().substring(0, 1).toUpperCase() + event.getLevel().name().substring(1).toLowerCase();
			message.append("  appender.").append(method).append("(\"").append(event.getMessage().getFormattedMessage()).append("\");\n");
		}
		message.append("\n");

		throw new AssertionError(message);
	}

}
