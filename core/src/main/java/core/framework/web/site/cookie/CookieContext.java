package core.framework.web.site.cookie;

import core.framework.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 */
public class CookieContext {
    private static final int DELETE_COOKIE_MAX_AGE = 0;
    private final Logger logger = LoggerFactory.getLogger(CookieContext.class);
    private final Map<String, String> cookies = new HashMap<>();
    private HttpServletResponse httpServletResponse;

    public <T> T getCookie(CookieSpec<T> spec) {
        String value = cookies.get(spec.name());
        return toValue(value, spec.type());
    }

    public <T> void setCookie(CookieSpec<T> spec, T value) {
        Cookie cookie = new Cookie(spec.name(), toCookieValue(value));
        setCookieAttributes(spec, cookie);

        AssertUtils.assertNotNull(httpServletResponse, "response is not injected, please check cookieInterceptor is added in WebConfig");
        httpServletResponse.addCookie(cookie);
        addCookie(cookie.getName(), cookie.getValue());
    }

    <T> void setCookieAttributes(CookieSpec<T> spec, Cookie cookie) {
        if (spec.pathAssigned())
            cookie.setPath(spec.path());
        if (spec.httpOnlyAssigned())
            cookie.setHttpOnly(spec.isHTTPOnly());
        if (spec.secureAssigned())
            cookie.setSecure(spec.isSecure());
        if (spec.maxAgeAssigned())
            cookie.setMaxAge((int) spec.maxAge().toSeconds());
    }

    public <T> void deleteCookie(CookieSpec spec) {
        Cookie cookie = new Cookie(spec.name(), null);
        cookie.setMaxAge(DELETE_COOKIE_MAX_AGE);
        cookie.setPath(spec.path());
        AssertUtils.assertNotNull(httpServletResponse, "response is not injected, please check cookieInterceptor is added in WebConfig");
        httpServletResponse.addCookie(cookie);
    }

    private <T> String toCookieValue(T value) {
        if (value == null) return null;
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    private <T> T toValue(String cookieValue, Class<T> type) {
        try {
            if (type.equals(String.class)) return (T) cookieValue;
            if (type.equals(Integer.class)) return (T) (Integer) Integer.parseInt(cookieValue);
        } catch (NumberFormatException e) {
            logger.warn("failed to convert cookie value, value={}, type={}", cookieValue, type, e);
            return null;
        }
        throw new IllegalStateException("unsupported cookie type, type=" + type);
    }

    void addCookie(String name, String value) {
        cookies.put(name, value);
    }

    void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
