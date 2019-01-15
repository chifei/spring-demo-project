package core.framework.exception;

import core.framework.log.ActionResult;
import core.framework.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @Inject
    TraceLogger traceLogger;

    public void handle(Throwable e) {
        if (ignore(e))
            return;
        if (warning(e)) {
            logWarning(e);
        } else {
            logError(e);
        }
    }

    boolean ignore(Throwable e) {
        return null == e || e.getClass().isAnnotationPresent(Ignore.class);
    }

    boolean warning(Throwable e) {
        return e.getClass().isAnnotationPresent(Warning.class) || isWarningException(e);
    }

    private boolean isWarningException(Throwable e) {
        return e instanceof HttpRequestMethodNotSupportedException
            || e instanceof BindException
            || e instanceof UnsatisfiedServletRequestParameterException
            || e instanceof MissingServletRequestParameterException
            || e instanceof ServletRequestBindingException
            || e instanceof MethodArgumentNotValidException
            || e instanceof HttpMediaTypeNotSupportedException
            || e instanceof HttpMessageNotReadableException;    // for invalid http message, like invalid json/xml
    }

    private void logError(Throwable e) {
        traceLogger.result(ActionResult.ERROR);
        logger.error(e.getMessage(), e);
        traceLogger.logContext("ex", e.getClass().getName());
        traceLogger.logContext("msg", e.getMessage());
    }

    private void logWarning(Throwable e) {
        traceLogger.result(ActionResult.WARNING);
        logger.info(e.getMessage(), e);
        traceLogger.logContext("ex", e.getClass().getName());
        traceLogger.logContext("msg", e.getMessage());
    }
}
