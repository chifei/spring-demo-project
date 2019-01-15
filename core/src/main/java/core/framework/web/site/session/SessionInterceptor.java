package core.framework.web.site.session;

import core.framework.internal.SpringObjectFactory;
import core.framework.util.AssertUtils;
import core.framework.web.site.ControllerHelper;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.cookie.CookieContext;
import core.framework.web.site.cookie.CookieInterceptor;
import core.framework.web.site.cookie.CookieSpec;
import core.framework.web.site.session.provider.LocalSessionProvider;
import core.framework.web.site.session.provider.RedisSessionProvider;
import core.framework.web.site.session.provider.SessionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * @author neo
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final CookieSpec<String> SESSION_ID = CookieSpec.stringKey("SessionId").path("/").sessionScope().httpOnly();
    private static final CookieSpec<String> SECURE_SESSION_ID = CookieSpec.stringKey("SecureSessionId").secure().path("/").sessionScope().httpOnly();

    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = SessionInterceptor.class.getName() + ".CONTEXT_INITIALIZED";
    private static final String BEAN_NAME_SESSION_PROVIDER = "sessionProvider";

    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Inject
    CookieContext cookieContext;
    @Inject
    SessionContext sessionContext;
    @Inject
    SecureSessionContext secureSessionContext;
    @Inject
    SiteSettings siteSettings;
    @Inject
    SpringObjectFactory springObjectFactory;
    @Inject
    CookieInterceptor cookieInterceptor;

    SessionProvider sessionProvider;

    @PostConstruct
    public void initialize() {
        SessionProviderType type = siteSettings.sessionProviderType();
        if (SessionProviderType.REDIS.equals(type)) {
            AssertUtils.assertHasText(siteSettings.remoteSessionServer(), "remote session server configuration is missing");
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, RedisSessionProvider.class);
        } else if (SessionProviderType.LOCAL.equals(type)) {
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, LocalSessionProvider.class);
        } else {
            throw new IllegalStateException("unsupported session provider type, type=" + type);
        }
        sessionProvider = springObjectFactory.bean(SessionProvider.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // only process non-forwarded request, to make sure only init once per request
        if (!initialized(request)) {
            logger.debug("initialize sessionContext");

            AssertUtils.assertTrue(cookieInterceptor.initialized(request), "sessionInterceptor depends on cookieInterceptor, please check WebConfig");
            String sessionId = cookieContext.getCookie(SESSION_ID);
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
                sessionContext.setId(sessionId);
                response.addCookie(new Cookie(SESSION_ID.name(), sessionId));
            }
            loadSession(sessionContext, SESSION_ID);
            if (request.isSecure()) {
                secureSessionContext.underSecureRequest();
                loadSession(secureSessionContext, SECURE_SESSION_ID);
            }
            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }
        return true;
    }

    private boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    private boolean requireSession(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, RequireSession.class) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        saveAllSessions(request);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // if some interceptor break the preHandle by returning false, all postHandle will be skipped.
        // by this way we want to try to save session on completion if possible
        // due to setCookies only works before view is rendered
        saveAllSessions(request);
    }

    private void saveAllSessions(HttpServletRequest request) {
        saveSession(sessionContext, SESSION_ID);
        if (request.isSecure()) {
            saveSession(secureSessionContext, SECURE_SESSION_ID);
        }
    }

    private void loadSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = cookieContext.getCookie(sessionCookie);
        if (sessionId != null) {
            Map<String, String> sessionData = sessionProvider.getAndRefresh(sessionId);
            if (sessionData != null) {
                sessionContext.setId(sessionId);
                sessionContext.loadSessionData(sessionData);
            } else {
                logger.debug("can not find session, generate new sessionId to replace old one");
                sessionContext.requireNewSessionId();
            }
        }
    }

    private void saveSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        if (sessionContext.changed()) {
            if (sessionContext.invalidated()) {
                deleteSession(sessionContext, sessionCookie);
            } else {
                persistSession(sessionContext, sessionCookie);
            }
            sessionContext.saved();
        }
    }

    private void deleteSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = sessionContext.id();
        if (sessionId == null) {
            // session was not persisted, nothing is required
            return;
        }
        sessionProvider.clear(sessionId);
        cookieContext.deleteCookie(sessionCookie);
    }

    private void persistSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = sessionContext.id();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            sessionContext.setId(sessionId);
            cookieContext.setCookie(sessionCookie, sessionId);
        }
        sessionProvider.save(sessionId, sessionContext.sessionData());
    }
}
