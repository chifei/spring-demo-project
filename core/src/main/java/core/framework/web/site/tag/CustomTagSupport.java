package core.framework.web.site.tag;

import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author neo
 */
public interface CustomTagSupport {
    void registerTags(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException;
}
