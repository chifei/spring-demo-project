package core.framework.web.site.exception;

import core.framework.exception.ErrorHandler;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.view.DefaultFreemarkerViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author neo
 */
public class ExceptionInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);
    @Inject
    DefaultFreemarkerViewResolver defaultFreemarkerViewResolver;
    @Inject
    SiteSettings siteSettings;
    @Inject
    ErrorPageModelBuilder errorPageModelBuilder;
    @Inject
    ErrorHandler errorHandler;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        errorHandler.handle(exception);

        if (renderCustomErrorPage(request, exception)) {
            logger.debug("render custom error page due to view error");
            if (response.isCommitted()) {
                logger.warn("response is committed, skip error page rendering");
                return;
            }
            try {
                response.reset();
                renderErrorPage(request, response, exception);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.error("failed to render error page", e);
            }
        }
    }

    private void renderErrorPage(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        View view = defaultFreemarkerViewResolver.resolveViewName(siteSettings.errorPage(), LocaleContextHolder.getLocale());

        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        view.render(model, request, response);
    }

    // only render error page if for request dispatch, to avoid duplicated process with forward dispatching
    private boolean renderCustomErrorPage(HttpServletRequest request, Exception exception) {
        return exception != null
            && DispatcherType.REQUEST.equals(request.getDispatcherType())
            && siteSettings.errorPage() != null;
    }
}
