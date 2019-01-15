package core.framework.web.site.tag;

import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.site.cdn.CDNSettings;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * freemarker url directive, used as "@cdn" in ftl
 *
 * @author neo
 */
public class CDNTag extends CDNTagSupport implements TemplateDirectiveModel {
    public CDNTag(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings) {
        super(request, runtimeSettings, cdnSettings);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String url = getRequiredStringParam(params, "value");

        String completeURL = constructCDNURL(url);
        Writer output = env.getOut();
        output.write(completeURL);
    }
}
