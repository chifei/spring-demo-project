package core.framework.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author neo
 */
public class FreemarkerTemplate {
    private final Logger logger = LoggerFactory.getLogger(FreemarkerTemplate.class);
    private final Configuration config;
    private final StringTemplateLoader loader;

    public FreemarkerTemplate() {
        config = new Configuration();

        loader = new StringTemplateLoader();

        config.setTemplateLoader(loader);
        config.setLocalizedLookup(false);
        config.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
    }

    public void putTemplate(String name, String template) {
        loader.putTemplate(name, template);
    }

    public String transform(String templateName, Map<String, Object> context) {
        logger.debug("transform by freemarker template, templateName={}", templateName);

        try {
            Template template = config.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(context, writer);
            return writer.toString();
        } catch (IOException | freemarker.template.TemplateException e) {
            throw new TemplateException(e);
        }
    }
}
