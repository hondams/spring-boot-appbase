package com.github.hondams.appbase.test.logging;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

public class LoggingEventExtension implements BeforeEachCallback, AfterEachCallback {

    private static ThreadLocal<Deque<ArgumentCaptor<ILoggingEvent>>> loggingEventCaptorsHolder =
            new ThreadLocal<>();

    private static ThreadLocal<Deque<Appender<ILoggingEvent>>> appendersHolder =
            new ThreadLocal<>();

    public static List<ILoggingEvent> getLoggingEvent() {
        Deque<ArgumentCaptor<ILoggingEvent>> loggingEventCaptors = loggingEventCaptorsHolder.get();
        if (loggingEventCaptors == null || loggingEventCaptors.isEmpty()) {
            return Collections.emptyList();
        }
        ArgumentCaptor<ILoggingEvent> loggingEventCaptor = loggingEventCaptors.peek();
        return Collections.unmodifiableList(loggingEventCaptor.getAllValues());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Deque<ArgumentCaptor<ILoggingEvent>> loggingEventCaptors = loggingEventCaptorsHolder.get();
        if (loggingEventCaptors == null) {
            loggingEventCaptors = new ArrayDeque<>();
            loggingEventCaptorsHolder.set(loggingEventCaptors);
        }
        Deque<Appender<ILoggingEvent>> appenders = appendersHolder.get();
        if (appenders == null) {
            appenders = new ArrayDeque<>();
            appendersHolder.set(appenders);
        }

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> appender = Mockito.mock(Appender.class);
        ArgumentCaptor<ILoggingEvent> loggingEventCaptor =
                ArgumentCaptor.forClass(ILoggingEvent.class);

        Mockito.doNothing().when(appender).doAppend(loggingEventCaptor.capture());

        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);

        loggingEventCaptors.add(loggingEventCaptor);
        appenders.add(appender);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Deque<ArgumentCaptor<ILoggingEvent>> loggingEventCaptors = loggingEventCaptorsHolder.get();
        if (loggingEventCaptors != null) {
            loggingEventCaptors.remove();
            if (loggingEventCaptors.isEmpty()) {
                loggingEventCaptorsHolder.remove();
            }
        }
        Deque<Appender<ILoggingEvent>> appenders = appendersHolder.get();
        if (appenders != null) {
            Appender<ILoggingEvent> appender = appenders.peek();
            if (appender != null) {
                Logger rootLogger =
                        (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
                rootLogger.detachAppender(appender);
            }
            appenders.remove();
            if (appenders.isEmpty()) {
                appendersHolder.remove();
            }
        }
    }
}
