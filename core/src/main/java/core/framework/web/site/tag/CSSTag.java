package core.framework.web.site.tag;

import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.cdn.CDNSettings;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * freemarker url directive, used as "@css" in ftl
 *
 * @author walter.zheng
 */
public class CSSTag extends CDNTagSupport implements TemplateDirectiveModel {
    public static final String TEMPLATE_CSS_TAG = "<link type=\"text/css\" rel=\"stylesheet\" href=\"%s\"%s/>";

    private final SiteSettings siteSettings;

    public CSSTag(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings, SiteSettings siteSettings) {
        super(request, runtimeSettings, cdnSettings);
        this.siteSettings = siteSettings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String tags = buildCSSTags(params);
        Writer output = env.getOut();
        output.write(tags);
    }

    String buildCSSTags(Map<String, Object> params) throws IOException, TemplateModelException {
        return buildMultipleResourceTags("href", siteSettings.cssDir(), TEMPLATE_CSS_TAG, params);
    }
}
