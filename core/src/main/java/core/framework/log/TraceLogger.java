package core.framework.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import core.framework.util.ExceptionUtils;
import core.framework.util.RuntimeIOException;

import java.io.IOException;

/**
 * @author neo
 */
public class TraceLogger implements ActionLogger {
    private static final TraceLogger INSTANCE = new TraceLogger();
    private final ThreadLocal<LoggingEventProcessor> processor = new ThreadLocal<>();
    private String logFolder;

    public static TraceLogger get() {
        return INSTANCE;
    }

    public void process(ILoggingEvent event) throws IOException {
        LoggingEventProcessor processor = this.processor.get();

        // ignore if current thread log is not explicitly initialized, for processor == null
        if (processor != null) {
            processor.process(event);
        }
    }

    public void initialize() {
        processor.set(new LoggingEventProcessor(logFolder));
    }

    @SuppressWarnings("PMD")    // fallback to system.err if logger failed
    public void cleanup() {
        try {
            LoggingEventProcessor processor = this.processor.get();
            processor.cleanup(LogSettings.get().alwaysWriteTraceLog());
            processor.actionLog.save();
            this.processor.remove();
        } catch (IOException e) {
            System.err.println("failed to clean up TraceLogger, exception=" + ExceptionUtils.stackTrace(e));
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public void logContext(String key, Object value) {
        ActionLog actionLog = processor.get().actionLog;
        actionLog.logContext(key, String.valueOf(value));
    }

    public String requestId() {
        return processor.get().actionLog.requestId;
    }

    public void requestId(String requestId) {
        ActionLog actionLog = processor.get().actionLog;
        actionLog.requestId(requestId);
    }

    public String action() {
        return processor.get().actionLog.action;
    }

    public void action(String action) {
        ActionLog actionLog = processor.get().actionLog;
        actionLog.action(action);
    }

    public void result(ActionResult result) {
        ActionLog actionLog = processor.get().actionLog;
        actionLog.result = result;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }
}
