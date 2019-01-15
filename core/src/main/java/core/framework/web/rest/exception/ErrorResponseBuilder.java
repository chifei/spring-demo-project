package core.framework.web.rest.exception;


import core.framework.util.ExceptionUtils;
import core.framework.web.runtime.RuntimeEnvironment;
import core.framework.web.runtime.RuntimeSettings;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ErrorResponseBuilder {
    @Inject
    RuntimeSettings runtimeSettings;

    public ErrorResponse createErrorResponse(Throwable e) {
        ErrorResponse error = new ErrorResponse();
        error.message = e.getMessage();
        error.exceptionClass = e.getClass().getName();
        if (runtimeSettings.environment() == RuntimeEnvironment.DEV) {
            error.exceptionTrace = ExceptionUtils.stackTrace(e);
        }
        return error;
    }
}