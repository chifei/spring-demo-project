package app.demo.common.web;

import com.google.common.collect.Maps;
import app.demo.common.exception.ResourceNotFoundException;
import app.demo.common.exception.UserAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler
    public String unauthenticated(UserAuthorizationException e) {
        return "redirect:/login";
    }

    @ExceptionHandler
    public ModelAndView error(ResourceNotFoundException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("/404.html", Maps.newHashMap());
    }
}
