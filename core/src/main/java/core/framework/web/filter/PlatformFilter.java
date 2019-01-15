package core.framework.web.filter;

import com.google.common.base.Charsets;
import core.framework.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author neo
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PlatformFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(PlatformFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding(Charsets.UTF_8.toString());
        TraceLogger traceLogger = TraceLogger.get();
        try {
            traceLogger.initialize();
            logger.debug("=== begin request processing ===");
            RequestWrapper requestWrapper = new RequestWrapper(request);
            requestWrapper.initialize();
            logRequest(requestWrapper, traceLogger);
            chain.doFilter(requestWrapper, response);
        } finally {
            logResponse(response, traceLogger);
            logger.debug("=== finish request processing ===");
            traceLogger.cleanup();
        }
    }

    private void logResponse(HttpServletResponse response, TraceLogger traceLogger) {
        int status = response.getStatus();
        traceLogger.logContext("status", status);
        logHeaders(response);
    }

    private void logHeaders(HttpServletResponse response) {
        for (String name : response.getHeaderNames()) {
            logger.debug("[header] {}={}", name, response.getHeader(name));
        }
    }

    private void logRequest(RequestWrapper request, TraceLogger traceLogger) throws IOException {
        logger.debug("request_url={}", request.getRequestURL());
        traceLogger.logContext("uri", request.relativeURLWithQueryString());
        logger.debug("server_port={}", request.getServerPort());
        logger.debug("context_path={}", request.getContextPath());
        traceLogger.logContext("method", request.getMethod());
        logger.debug("dispatcher_type={}", request.getDispatcherType());
        logger.debug("local_port={}", request.getLocalPort());
        logHeaders(request);
        logParameters(request);
        logger.debug("remote_address={}", request.getRemoteAddr());
        traceLogger.logContext("ip", request.clientIP());

        if (request.preLoadBody()) {
            logger.debug("body={}", request.body());
        }
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = (String) headers.nextElement();
            logger.debug("[header] {}={}", headerName, request.getHeader(headerName));
        }
    }

    void logParameters(HttpServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            logger.debug("[param] {}={}", paramName, request.getParameter(paramName));
        }
    }

    @Override
    public void destroy() {
    }
}
