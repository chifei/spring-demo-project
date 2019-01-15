package core.framework.scheduler;

import core.framework.exception.ErrorHandler;
import core.framework.internal.ClassUtils;
import core.framework.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author neo
 */
public class JobProxy implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(JobProxy.class);
    private final Job job;
    @Inject
    TraceLogger traceLogger;
    @Inject
    ErrorHandler errorHandler;

    public JobProxy(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        try {
            traceLogger.initialize();
            traceLogger.action(ClassUtils.getSimpleOriginalClassName(job));
            traceLogger.requestId(UUID.randomUUID().toString());
            logger.debug("=== start job execution ===");
            traceLogger.logContext("job_class", job.getClass().getName());
            job.execute();
        } catch (Throwable e) {
            errorHandler.handle(e);
        } finally {
            logger.debug("=== finish job execution ===");
            traceLogger.cleanup();
        }
    }

    @Override
    public String toString() {
        return String.format("JobProxy{job=%s}", job);
    }
}
