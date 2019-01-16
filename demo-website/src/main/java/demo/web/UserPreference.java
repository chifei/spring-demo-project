package demo.web;

import com.google.common.base.Strings;
import core.framework.web.site.cookie.CookieContext;
import core.framework.web.site.cookie.CookieSpec;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import java.util.Locale;

/**
 * @author chi
 */
@Component
public class UserPreference {
    private static final CookieSpec<String> COOKIE_LANGUAGE = CookieSpec.stringKey("lang");
    @Inject
    CookieContext cookieContext;
    @Inject
    ServletRequest servletRequest;

    public Locale locale() {
        String language = cookieContext.getCookie(COOKIE_LANGUAGE);
        if (!Strings.isNullOrEmpty(language)) {
            return Locale.forLanguageTag(language);
        }
        language = servletRequest.getParameter("lang");
        if (!Strings.isNullOrEmpty(language)) {
            return Locale.forLanguageTag(language);
        }
        return Locale.getDefault();
    }
}
