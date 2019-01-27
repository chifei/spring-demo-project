package app.demo.common.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String error() {
        return "/500.html";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
