package core.framework.log;


/**
 * @author neo
 */
public class LogSettings {
    private static final LogSettings INSTANCE = new LogSettings();
    private boolean alwaysWriteTraceLog;
    private LogMessageFilter logMessageFilter;

    public static LogSettings get() {
        return INSTANCE;
    }

    public boolean alwaysWriteTraceLog() {
        return alwaysWriteTraceLog;
    }

    public void setAlwaysWriteTraceLog(boolean alwaysWriteTraceLog) {
        this.alwaysWriteTraceLog = alwaysWriteTraceLog;
    }

    public LogMessageFilter logMessageFilter() {
        return logMessageFilter;
    }

    public void filterLog(LogMessageFilter logMessageFilter) {
        this.logMessageFilter = logMessageFilter;
    }
}
