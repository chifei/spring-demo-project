package demo.web;

import com.google.common.collect.Maps;
import core.framework.exception.ResourceNotFoundException;
import demo.web.exception.UnauthenticatedException;
import demo.web.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chi
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public String unauthenticated(UnauthenticatedException e) {
        return "redirect:/login";
    }

    @ExceptionHandler
    public ModelAndView unauthorized(UnauthorizedException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return new ModelAndView("/403.html", Maps.newHashMap());
    }

    @ExceptionHandler
    public ModelAndView error(ResourceNotFoundException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("/404.html", Maps.newHashMap());
    }
}
