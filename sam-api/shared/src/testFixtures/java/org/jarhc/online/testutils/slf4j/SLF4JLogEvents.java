package org.jarhc.online.testutils.slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.event.Level;

public class SLF4JLogEvents {

	static final SLF4JLogEvents INSTANCE = new SLF4JLogEvents();

	private final List<LogEvent> events = new ArrayList<>();

	private SLF4JLogEvents() {
	}

	void addEvent(LogEvent event) {
		events.add(event);
	}

	public List<LogEvent> getEvents() {
		return events;
	}

	public void clearEvents() {
		events.clear();
	}

	// assertions --------------------------------------------------------------

	// TODO: support assertions for throwable

	public SLF4JLogEvents assertTrace(String message) {
		return assertEvent(Level.TRACE, message);
	}

	public SLF4JLogEvents assertDebug(String message) {
		return assertEvent(Level.DEBUG, message);
	}

	public SLF4JLogEvents assertInfo(String message) {
		return assertEvent(Level.INFO, message);
	}

	public SLF4JLogEvents assertWarn(String message) {
		return assertEvent(Level.WARN, message);
	}

	public SLF4JLogEvents assertError(String message) {
		return assertEvent(Level.ERROR, message);
	}

	public SLF4JLogEvents assertEvent(Level level, String message) {
		for (Iterator<LogEvent> iterator = events.iterator(); iterator.hasNext(); ) {
			LogEvent event = iterator.next();
			if (event.getLevel().equals(level) && event.getMessage().equals(message)) {
				iterator.remove();
				return this;
			}
		}
		throw new AssertionError("No " + level + " event found with message: " + message);
	}

	public SLF4JLogEvents assertNoEvents() {
		if (events.isEmpty()) {
			return this;
		}
		StringBuilder message = new StringBuilder("Unexpected log events:\n");
		for (LogEvent event : events) {
			message.append("- ");
			message.append(event.getLevel());
			message.append(": ");
			message.append(event.getMessage());
			message.append("\n");
		}

		message.append("Missing assertions:\n");
		for (LogEvent event : events) {
			String method = "assert" + event.getLevel().name().substring(0, 1).toUpperCase() + event.getLevel().name().substring(1).toLowerCase();
			message.append("  appender.").append(method).append("(\"").append(event.getMessage()).append("\");\n");
		}
		message.append("\n");

		throw new AssertionError(message);
	}

}
