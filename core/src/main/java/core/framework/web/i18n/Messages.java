package core.framework.web.i18n;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author neo
 */
public class Messages extends ReloadableResourceBundleMessageSource {
    public String getMessage(String key, Object... arguments) {
        return super.getMessage(key, arguments, key, Locale.getDefault());
    }

    public Map<String, String> getMessages(Locale locale) {
        Map<String, String> messages = new HashMap<>();
        Properties properties = getMergedProperties(locale).getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                messages.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return messages;
    }

    protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
        List<String> result = new ArrayList<String>(3);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder temp = new StringBuilder(ResourceUtils.CLASSPATH_URL_PREFIX).append(basename);

        temp.append('_');
        if (language.length() > 0) {
            temp.append(language);
            result.add(0, temp.toString());
        }

        temp.append('_');
        if (country.length() > 0) {
            temp.append(country);
            result.add(0, temp.toString());
        }

        if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
            temp.append('_').append(variant);
            result.add(0, temp.toString());
        }

        return result;
    }
}
