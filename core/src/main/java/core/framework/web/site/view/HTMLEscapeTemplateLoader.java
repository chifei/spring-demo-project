package core.framework.web.site.view;

import core.framework.util.IOUtils;
import freemarker.cache.TemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author neo
 */
public class HTMLEscapeTemplateLoader implements TemplateLoader {
    private final TemplateLoader templateLoader;

    public HTMLEscapeTemplateLoader(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return templateLoader.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return templateLoader.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Reader reader = templateLoader.getReader(templateSource, encoding);
        return new StringReader("<#escape x as x?html>" + IOUtils.text(reader) + "</#escape>");
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        templateLoader.closeTemplateSource(templateSource);
    }
}
