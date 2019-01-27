package app.demo.common.web;


import app.demo.common.web.interceptor.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @LoginRequired
    public String index() {
        return "redirect:/admin/";
    }
}
