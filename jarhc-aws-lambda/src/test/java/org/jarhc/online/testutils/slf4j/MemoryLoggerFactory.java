package org.jarhc.online.testutils.slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

class MemoryLoggerFactory implements ILoggerFactory {

	private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();

	public Logger getLogger(String name) {
		Logger instance = loggers.get(name);
		if (instance != null) {
			return instance;
		} else {
			Logger newInstance = new MemoryLogger(name);
			Logger oldInstance = loggers.putIfAbsent(name, newInstance);
			return oldInstance == null ? newInstance : oldInstance;
		}
	}

}
