package core.framework.web.site.tag;

import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.cdn.CDNSettings;
import core.framework.web.site.view.DefaultFreemarkerView;
import core.framework.web.site.view.DefaultFreemarkerViewResolver;
import freemarker.template.TemplateModelException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * @author neo
 */
public class DefaultTagSupport {
    @Inject
    SiteSettings siteSettings;
    @Inject
    CDNSettings cdnSettings;
    @Inject
    RuntimeSettings runtimeSettings;
    @Inject
    DefaultFreemarkerViewResolver viewResolver;

    public void registerTags(DefaultFreemarkerView view, Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        registerURLTag(model, request);
        registerCDNTag(model, request);
        registerMasterTag(model, request, view);
        registerJSTag(model, request);
        registerCSSTag(model, request);
    }

    void assertTagNameIsAvailable(Object previousValue, String tagName) throws TemplateModelException {
        if (previousValue != null)
            throw new TemplateModelException(String.format("%1$s is reserved name in model as @%1$s, please use different name in model", tagName));
    }

    private void registerURLTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_URL, new URLTag(request));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_URL);
    }

    private void registerJSTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_JS, new JSTag(request, runtimeSettings, cdnSettings, siteSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_JS);
    }

    private void registerCSSTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_CSS, new CSSTag(request, runtimeSettings, cdnSettings, siteSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_CSS);
    }

    private void registerCDNTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_CDN, new CDNTag(request, runtimeSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_CDN);
    }

    private void registerMasterTag(Map<String, Object> model, HttpServletRequest request, DefaultFreemarkerView view) throws TemplateModelException {
        Locale locale = RequestContextUtils.getLocale(request);
        MasterTemplateLoader templateLoader = new MasterTemplateLoader(viewResolver, view, locale);
        Object previousValue = model.put(TagNames.TAG_MASTER, new MasterTag(model, templateLoader));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_MASTER);

        previousValue = model.put(TagNames.TAG_BODY, new BodyTag(model));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_BODY);
    }
}
