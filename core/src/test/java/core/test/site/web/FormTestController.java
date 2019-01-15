package core.test.site.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author neo
 */
@Controller
public class FormTestController {
    @RequestMapping(value = "/test/form", method = RequestMethod.POST)
    public String submit(TestForm form, Map<String, Object> model) {
        model.put("name", form.getName());
        return "/form";
    }
}
