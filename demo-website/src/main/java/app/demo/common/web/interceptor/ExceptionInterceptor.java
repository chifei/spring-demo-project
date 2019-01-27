package app.demo.common.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;


public class ExceptionInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        if (renderCustomErrorPage(request, exception)) {
            logger.debug("render custom error page due to view error");
            if (response.isCommitted()) {
                logger.warn("response is committed, skip error page rendering");
                return;
            }
            try {
                response.reset();
                renderErrorPage(response, exception);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.error("failed to render error page", e);
            }
        }
    }

    private void renderErrorPage(HttpServletResponse response, Exception exception) throws Exception {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exception.printStackTrace(new PrintStream(response.getOutputStream()));
    }

    // only render error page if for request dispatch, to avoid duplicated process with forward dispatching
    private boolean renderCustomErrorPage(HttpServletRequest request, Exception exception) {
        return exception != null
            && DispatcherType.REQUEST.equals(request.getDispatcherType());
    }
}
