package core.framework.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * @author neo
 */
public class TraceAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Override
    public void start() {
        super.start();
        FilterMessagePatternLayout.get().setContext(getContext());
        FilterMessagePatternLayout.get().start();
    }

    @Override
    public void stop() {
        super.stop();
        FilterMessagePatternLayout.get().stop();
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            TraceLogger.get().process(event);
        } catch (Exception e) {
            addError("failed to write log", e);
        }
    }

    // set in logback.xml
    public void setLogFolder(String logFolder) {
        TraceLogger.get().setLogFolder(logFolder);
    }
}
