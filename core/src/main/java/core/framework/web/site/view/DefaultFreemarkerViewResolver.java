package core.framework.web.site.view;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author neo
 */
public class DefaultFreemarkerViewResolver extends FreeMarkerViewResolver {
    public String buildFullTemplatePath(String template) {
        return String.format("%s%s%s", getPrefix(), template, getSuffix());
    }
}
