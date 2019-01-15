package core.framework.web.site;

import core.framework.exception.ResourceNotFoundException;
import core.framework.exception.SessionTimeOutException;
import core.framework.web.i18n.Messages;
import core.framework.web.request.RequestContext;
import core.framework.web.site.exception.ErrorPageModelBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * This class defined all the global exception handler and model attribute for site
 *
 * @author neo
 */
@ControllerAdvice
public class SiteErrorControllerAdvice {
    @Inject
    Messages messages;
    @Inject
    SiteSettings siteSettings;
    @Inject
    ErrorPageModelBuilder errorPageModelBuilder;
    @Inject
    RequestContext requestContext;

    @ExceptionHandler
    public ModelAndView error(Throwable exception, HttpServletResponse response) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        String errorPage = siteSettings.errorPage();
        setStatus(errorPage, response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ModelAndView(errorPage, model);
    }

    // method not allowed exception throws from outside of controller, so defined here
    @ExceptionHandler
    public ModelAndView methodNotAllowed(HttpRequestMethodNotSupportedException exception, HttpServletResponse response) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        String resourceNotFoundPage = siteSettings.resourceNotFoundPage();
        setStatus(resourceNotFoundPage, response, HttpStatus.METHOD_NOT_ALLOWED);
        return new ModelAndView(resourceNotFoundPage, model);
    }

    @ModelAttribute("requestContext")
    public RequestContext requestContext() {
        return requestContext;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView notFound(ResourceNotFoundException exception, HttpServletResponse response) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        String resourceNotFoundPage = siteSettings.resourceNotFoundPage();
        setStatus(resourceNotFoundPage, response, HttpStatus.NOT_FOUND);
        return new ModelAndView(resourceNotFoundPage, model);
    }

    // due to spring RedirectView use @ResponseStatus with http10Compatible=false, set status code explicitly
    private void setStatus(String view, HttpServletResponse response, HttpStatus status) {
        if (view != null && view.startsWith("redirect:")) response.setStatus(HttpStatus.SEE_OTHER.value());
        else response.setStatus(status.value());
    }

    @ExceptionHandler(SessionTimeOutException.class)
    public ModelAndView sessionTimeOut(SessionTimeOutException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.sessionTimeOutPage(), model);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView validationError(MethodArgumentNotValidException exception, HttpServletResponse response) {
        return validationErrorPage(exception, exception.getBindingResult(), response);
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView validationError(BindException exception, HttpServletResponse response) {
        return validationErrorPage(exception, exception.getBindingResult(), response);
    }

    private ModelAndView validationErrorPage(Exception exception, BindingResult bindingResult, HttpServletResponse response) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(buildValidationErrorMessage(bindingResult), exception);
        String errorPage = siteSettings.errorPage();
        setStatus(errorPage, response, HttpStatus.BAD_REQUEST);
        return new ModelAndView(errorPage, model);
    }

    private String buildValidationErrorMessage(BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        StringBuilder builder = new StringBuilder(120);
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getObjectName())
                .append('.')
                .append(fieldError.getField())
                .append(" => ")
                .append(messages.getMessage(fieldError, locale))
                .append(", rejectedValue=")
                .append(fieldError.getRejectedValue())
                .append('\n');
        }
        return builder.toString();
    }
}
