package core.framework.web.site.tag;

import core.framework.web.site.view.DefaultFreemarkerView;
import core.framework.web.site.view.DefaultFreemarkerViewResolver;
import freemarker.template.Template;

import java.io.IOException;
import java.util.Locale;

/**
 * load template consistently as spring freemarker view and view resolver
 *
 * @author neo
 */
public class MasterTemplateLoader {
    private final DefaultFreemarkerViewResolver viewResolver;
    private final DefaultFreemarkerView view;
    private final Locale locale;

    public MasterTemplateLoader(DefaultFreemarkerViewResolver viewResolver, DefaultFreemarkerView view, Locale locale) {
        this.viewResolver = viewResolver;
        this.view = view;
        this.locale = locale;
    }

    public Template loadTemplate(String template) throws IOException {
        String fullTemplatePath = viewResolver.buildFullTemplatePath(template);
        return view.loadTemplate(fullTemplatePath, locale);
    }
}
