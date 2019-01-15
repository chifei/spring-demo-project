package core.framework.log;

import core.framework.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author neo
 */
public class LoggingEventProcessorTest {
    LoggingEventProcessor processor;

    @Before
    public void createLoggingEventProcessor() {
        processor = new LoggingEventProcessor("/log");
    }

    @Test
    public void generateLogFilePath() {
        String logFilePath = processor.generateLogFilePath("someController-method", DateUtils.date(2012, Calendar.OCTOBER, 2, 14, 5, 0), "requestId");
        assertThat(logFilePath, containsString("/log/2012/10/02/someController-method/1405.requestId."));
    }
}
