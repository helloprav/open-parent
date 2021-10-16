package org.openframework.commons.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

public class LogbackUtils {

	public LogbackUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static void setLevel(String level, String loggerName) {
		Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		logger.setLevel(Level.valueOf(level));
	}

	public static List<ch.qos.logback.classic.Logger> findNamesOfConfiguredLoggers() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		List<ch.qos.logback.classic.Logger> loggerList = new ArrayList<>();
		for (ch.qos.logback.classic.Logger log : lc.getLoggerList()) {
			if (log.getLevel() != null || hasAppenders(log)) {
				loggerList.add(log);
			}
		}
		return loggerList;
	}

	public static boolean hasAppenders(ch.qos.logback.classic.Logger logger) {
		Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
		return it.hasNext();
	}

}
