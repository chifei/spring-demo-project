package demo.web;


import demo.web.interceptor.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author neo
 */
@Controller
public class AdminController {
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @LoginRequired
    public String index(Map<String, Object> model) {
        model.put("welcome", "hello world");
        return "/app.html";
    }
}
