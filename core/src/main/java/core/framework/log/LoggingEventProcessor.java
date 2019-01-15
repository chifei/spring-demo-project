package core.framework.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.common.base.Charsets;
import core.framework.util.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author neo
 */
public class LoggingEventProcessor {
    private static final int MAX_HOLD_SIZE = 5000;
    final ActionLog actionLog = new ActionLog();
    private final String logFolder;
    private final List<ILoggingEvent> events = new ArrayList<>();
    private boolean hold = true;
    private Writer writer;

    public LoggingEventProcessor(String logFolder) {
        this.logFolder = logFolder;
    }

    public void process(ILoggingEvent event) throws IOException {
        if (hold) {
            events.add(event);
            if (flushLog(event)) {
                flushTraceLogs();
                hold = false;
            }
        } else {
            write(event);
        }
    }

    private boolean flushLog(ILoggingEvent event) {
        return event.getLevel().isGreaterOrEqual(Level.WARN) || events.size() > MAX_HOLD_SIZE;
    }

    private void flushTraceLogs() throws IOException {
        if (writer == null)
            writer = createWriter();

        for (ILoggingEvent event : events) {
            write(event);
        }
        events.clear();
    }

    void write(ILoggingEvent event) throws IOException {
        String log = FilterMessagePatternLayout.get().doLayout(event);
        writer.write(log);
    }

    private Writer createWriter() throws FileNotFoundException {
        if (logFolder == null)
            return new BufferedWriter(new OutputStreamWriter(System.err, Charsets.UTF_8));

        String logFilePath = generateLogFilePath(actionLog.action, actionLog.requestDate, actionLog.requestId);
        actionLog.logContext("log_path", logFilePath);
        File logFile = new File(logFilePath);
        createParentFolder(logFile);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), Charsets.UTF_8));
    }

    private void createParentFolder(File logFile) {
        File folder = logFile.getParentFile();
        folder.mkdirs();
    }

    void cleanup(boolean forceFlushTraceLog) throws IOException {
        if (forceFlushTraceLog) {
            flushTraceLogs();
        }
        if (logFolder == null) {
            IOUtils.flush(writer); // do not close System.err (when logFolder is null)
        } else {
            IOUtils.close(writer);
        }
    }

    String generateLogFilePath(String action, Date targetDate, String requestId) {
        String sequence = RandomStringUtils.randomAlphanumeric(5);

        return String.format("%1$s/%2$tY/%2$tm/%2$td/%3$s/%2$tH%2$tM.%4$s.%5$s.log",
            logFolder, targetDate, action == null ? "unknown" : action, requestId == null ? "unknown" : requestId, sequence);
    }
}
