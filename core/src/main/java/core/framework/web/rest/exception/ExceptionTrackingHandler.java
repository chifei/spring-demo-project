package core.framework.web.rest.exception;

import core.framework.exception.ErrorHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * track exception from handlerMethod
 *
 * @author neo
 */
public class ExceptionTrackingHandler extends AbstractHandlerExceptionResolver {
    @Inject
    ErrorHandler errorHandler;

    public ExceptionTrackingHandler() {
        setOrder(HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        errorHandler.handle(e);
        return null;
    }
}
