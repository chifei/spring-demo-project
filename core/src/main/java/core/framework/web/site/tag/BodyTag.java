package core.framework.web.site.tag;

import core.framework.util.AssertUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

/**
 * body tag used in master template to locate where to insert the body
 *
 * @author neo
 */
public class BodyTag implements TemplateDirectiveModel {
    static final String BODY_TEMPLATE = "_master_body_template";

    private final Map<String, Object> model;

    public BodyTag(Map<String, Object> model) {
        this.model = model;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        TemplateDirectiveBody bodyDirective = (TemplateDirectiveBody) model.get(BODY_TEMPLATE);
        AssertUtils.assertNotNull(bodyDirective, "body template is null, make sure MasterTag puts it");
        bodyDirective.render(env.getOut());
    }
}
