package org.jarhc.online.testutils.slf4j;

import org.slf4j.event.Level;

public class LogEvent {

	private final Level level;
	private final String message;
	private final Throwable throwable;

	LogEvent(Level level, String message, Throwable throwable) {
		this.level = level;
		this.message = message;
		this.throwable = throwable;
	}

	public Level getLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}
