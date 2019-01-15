package core.framework.web.request;

import core.framework.internal.ClassUtils;
import core.framework.log.TraceLogger;
import core.framework.util.StringUtils;
import core.framework.web.filter.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * @author neo
 */
public class RequestContextInterceptor extends HandlerInterceptorAdapter {
    public static final String HEADER_REQUEST_ID = "request-id";
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = RequestContextInterceptor.class.getName() + ".CONTEXT_INITIALIZED";

    private final Logger logger = LoggerFactory.getLogger(RequestContextInterceptor.class);

    @Inject
    TraceLogger traceLogger;

    @Inject
    RequestContextImpl requestContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // make sure only init once per request
        if (!initialized(request)) {
            initializeRequestContext(request);
            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }

        assignAction(handler);

        return true;
    }

    public boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    // use the first handleMethod in call stack, due to http forwarding, the stack can be view->handlerMethod->handlerMethod
    void assignAction(Object handler) {
        String currentHandler = handler(handler);
        logger.debug("current_handler={}", currentHandler);

        if (traceLogger.action() == null && handler instanceof HandlerMethod) {
            traceLogger.action(currentHandler);
        }
    }

    private String handler(Object handler) {
        if (handler instanceof HandlerMethod) {
            return String.format("%s-%s", ClassUtils.getSimpleOriginalClassName(((HandlerMethod) handler).getBean()), ((HandlerMethod) handler).getMethod().getName());
        } else if (handler instanceof ParameterizableViewController) {
            return ((ParameterizableViewController) handler).getViewName();
        }
        throw new IllegalStateException("unknown handler, handler=" + handler);
    }

    private void initializeRequestContext(HttpServletRequest request) {
        logger.debug("initialize requestContext");

        if (request instanceof AbstractMultipartHttpServletRequest) {
            requestContext.setHTTPRequest((RequestWrapper) ((AbstractMultipartHttpServletRequest) request).getRequest());
        } else {
            requestContext.setHTTPRequest((RequestWrapper) request);
        }
        String requestId = requestId(request);

        requestContext.setRequestId(requestId);
        requestContext.setRequestDate(new Date());

        traceLogger.requestId(requestContext.requestId());
    }

    private String requestId(HttpServletRequest request) {
        String requestIdFromHeader = request.getHeader(HEADER_REQUEST_ID);
        if (StringUtils.hasText(requestIdFromHeader)) {
            RequestIdValidator.validateRequestId(requestIdFromHeader);
            return requestIdFromHeader;
        }
        logger.debug("request headers do not contain request-id, generate new one");
        return UUID.randomUUID().toString();
    }
}

