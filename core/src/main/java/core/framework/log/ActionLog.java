package core.framework.log;

import core.framework.util.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author neo
 */
public class ActionLog {
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
    private static final String LOG_SPLITTER = " | ";

    public final Date requestDate = new Date();
    private final Logger logger = LoggerFactory.getLogger(ActionLog.class);
    private final Map<String, String> context = new TreeMap<>();
    public String requestId;
    public String action;
    public ActionResult result = ActionResult.SUCCESS;

    public void logContext(String key, String value) {
        logger.debug("{}={}", key, value);
        context.put(key, value);
    }

    public void requestId(String requestId) {
        this.requestId = requestId;
        logger.debug("request_id={}", requestId);
    }

    public void action(String action) {
        this.action = action;
        logger.debug("action={}", action);
    }

    public void save() {
        logContext("elapsed", String.valueOf(System.currentTimeMillis() - requestDate.getTime()));
        LoggerFactory.getLogger("action").info(buildActionLog());
    }

    String buildActionLog() {
        StringBuilder builder = new StringBuilder();
        builder.append(timestamp())
            .append(LOG_SPLITTER)
            .append(result)
            .append(LOG_SPLITTER)
            .append(requestId == null ? "unknown" : requestId)
            .append(LOG_SPLITTER)
            .append(action == null ? "unknown" : action);

        for (Map.Entry<String, String> entry : context.entrySet()) {
            String value = filterLineSeparator(entry.getValue());
            builder.append(LOG_SPLITTER)
                .append(entry.getKey()).append('=').append(value);
        }

        return builder.toString();
    }

    String timestamp() {
        return Convert.toString(requestDate, LOG_DATE_FORMAT);
    }

    String filterLineSeparator(String value) {
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '\n' || ch == '\r') builder.append(' ');
            else builder.append(ch);
        }
        return builder.toString();
    }
}
