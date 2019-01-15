package core.framework.web.site.view;

import core.framework.web.site.SiteSettings;
import core.framework.web.site.tag.CustomTagSupport;
import core.framework.web.site.tag.DefaultTagSupport;
import freemarker.template.Template;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author neo
 */
public class DefaultFreemarkerView extends FreeMarkerView {
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = DefaultFreemarkerView.class.getName() + ".CONTEXT_INITIALIZED";

    DefaultTagSupport defaultTagSupport;
    CustomTagSupport customTagSupport;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        ApplicationContext applicationContext = getApplicationContext();
        SiteSettings siteSettings = applicationContext.getBean(SiteSettings.class);
        defaultTagSupport = applicationContext.getBean(DefaultTagSupport.class);
        Class<? extends CustomTagSupport> customTagSupportClass = siteSettings.customTagSupport();
        if (customTagSupportClass != null) {
            customTagSupport = applicationContext.getBean(customTagSupportClass);
        }
    }

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        // if exception occurs in rendering view, since the attribute will be exposed to model, so the tag can be registered before
        if (!initialized(request)) {
            defaultTagSupport.registerTags(this, model, request);

            if (customTagSupport != null) {
                customTagSupport.registerTags(model, request);
            }

            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }
    }

    private boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    public Template loadTemplate(String fullTemplatePath, Locale locale) throws IOException {
        return getTemplate(fullTemplatePath, locale);
    }
}
