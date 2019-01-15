package core.framework.web.config;

import core.framework.exception.ErrorHandler;
import core.framework.internal.SpringObjectFactory;
import core.framework.log.TraceLogger;
import core.framework.scheduler.Scheduler;
import core.framework.web.handler.ResourceNotFoundHandlerMapping;
import core.framework.web.request.RequestContextImpl;
import core.framework.web.rest.RESTControllerAdvice;
import core.framework.web.rest.exception.ErrorResponseBuilder;
import core.framework.web.rest.exception.ExceptionTrackingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author neo
 */
public class DefaultConfig {
    @Bean
    TraceLogger traceLogger() {
        return TraceLogger.get();
    }

    @Bean
    ErrorResponseBuilder errorResponseBuilder() {
        return new ErrorResponseBuilder();
    }

    @Bean
    RESTControllerAdvice restControllerAdvice() {
        return new RESTControllerAdvice();
    }

    @Bean
    ExceptionTrackingHandler exceptionTrackingHandler() {
        return new ExceptionTrackingHandler();
    }

    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    ResourceNotFoundHandlerMapping resourceNotFoundHandlerMapping() {
        return new ResourceNotFoundHandlerMapping();
    }

    @Bean
    SpringObjectFactory springObjectFactory() {
        return new SpringObjectFactory();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    RequestContextImpl requestContext() {
        return new RequestContextImpl();
    }

    @Bean
    Scheduler scheduler() {
        return new Scheduler();
    }
}
