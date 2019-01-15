package core.framework.web.site.scheme;

import core.framework.util.AssertUtils;
import core.framework.web.request.RequestContext;
import core.framework.web.request.RequestContextInterceptor;
import core.framework.web.site.ControllerHelper;
import core.framework.web.site.url.URLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author neo
 */
public class HTTPSchemeEnforceInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(HTTPSchemeEnforceInterceptor.class);

    @Inject
    RequestContextInterceptor requestContextInterceptor;
    @Inject
    RequestContext requestContext;

    // http scheme enforce interceptor need to handle both request and forward dispatcher, because of "forward:" view
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AssertUtils.assertTrue(requestContextInterceptor.initialized(request), "httpSchemeEnforceInterceptor depends on requestContextInterceptor, please check WebConfig");

        HTTPOnly httpOnly = ControllerHelper.findMethodOrClassLevelAnnotation(handler, HTTPOnly.class);
        if (httpOnly != null && !"http".equals(request.getScheme())) {
            enforceScheme(request, response, "http");
            return false;
        }

        HTTPSOnly httpsOnly = ControllerHelper.findMethodOrClassLevelAnnotation(handler, HTTPSOnly.class);
        if (httpsOnly != null && !"https".equals(request.getScheme())) {
            enforceScheme(request, response, "https");
            return false;
        }
        return true;
    }

    private void enforceScheme(HttpServletRequest request, HttpServletResponse response, String scheme) {
        URLBuilder builder = new URLBuilder();

        builder.setScheme(scheme);
        builder.setServerName(request.getServerName());
        builder.setContextPath(request.getContextPath());
        builder.setLogicalURL(requestContext.relativeURLWithQueryString());

        String redirectURL = builder.buildFullURL();
        logger.debug("redirect to different scheme, redirectURL={}", redirectURL);

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", redirectURL);
    }
}
