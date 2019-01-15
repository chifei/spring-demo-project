package core.framework.web.site.exception;

import core.framework.util.ExceptionUtils;

/**
 * @author neo
 */
public class ExceptionInfo {
    private final String message;
    private final String stackTrace;

    public ExceptionInfo(String message, Throwable e) {
        this.message = message;
        stackTrace = ExceptionUtils.stackTrace(e);
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
