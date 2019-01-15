package core.framework.task;

import core.framework.exception.ErrorHandler;
import core.framework.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author neo
 */
public class TaskProxy<T> implements Callable<T> {
    private final Logger logger = LoggerFactory.getLogger(TaskProxy.class);

    private final Callable<T> delegate;
    private final TraceLogger traceLogger;
    private final ErrorHandler errorHandler;
    private final String requestId;
    private final String action;

    public TaskProxy(Callable<T> delegate, TraceLogger traceLogger, ErrorHandler errorHandler) {
        this.delegate = delegate;
        this.traceLogger = traceLogger;
        this.errorHandler = errorHandler;
        action = traceLogger.action() + "/" + delegate.getClass().getName();
        requestId = traceLogger.requestId();
    }

    @Override
    public T call() throws Exception {
        try {
            traceLogger.initialize();
            traceLogger.action(action);
            traceLogger.requestId(requestId);
            logger.debug("start task, task={}, currentThread={}", delegate, Thread.currentThread().getId());
            return delegate.call();
        } catch (Exception e) {
            errorHandler.handle(e);
            throw e;
        } finally {
            logger.debug("end task, task={}, currentThread={}", delegate, Thread.currentThread().getId());
            traceLogger.cleanup();
        }
    }
}
