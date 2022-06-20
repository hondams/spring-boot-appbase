package com.github.hondams.appbase.test.sample.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.hondams.appbase.test.logging.LoggingEventExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(LoggingEventExtension.class)
@Slf4j
class LoggingEventExtensionTest {

    @Test
    void test() {
        Iterator<ILoggingEvent> loggingEventsIterator =
                LoggingEventExtension.getLoggingEvent().iterator();
        assertFalse(loggingEventsIterator.hasNext());
    }

    @Test
    void test2() {
        log.info("test");
        Iterator<ILoggingEvent> loggingEventsIterator =
                LoggingEventExtension.getLoggingEvent().iterator();
        assertTrue(loggingEventsIterator.hasNext());
        {
            ILoggingEvent loggingEvent = loggingEventsIterator.next();
            assertEquals(Level.INFO, loggingEvent.getLevel());
            assertEquals("test", loggingEvent.getMessage());
            assertEquals(LoggingEventExtensionTest.class.getName(), loggingEvent.getLoggerName());
        }
        assertFalse(loggingEventsIterator.hasNext());
    }
}
