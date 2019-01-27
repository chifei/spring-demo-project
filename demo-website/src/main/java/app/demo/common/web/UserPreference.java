package app.demo.common.web;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Component
public class UserPreference {
    @Inject
    HttpServletRequest request;

    public Locale locale() {
        String language = request.getParameter("lang");
        if (!Strings.isNullOrEmpty(language)) {
            return Locale.forLanguageTag(language);
        }
        return Locale.getDefault();
    }
}
