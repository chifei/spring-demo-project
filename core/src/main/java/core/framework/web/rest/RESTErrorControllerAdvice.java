package core.framework.web.rest;

import core.framework.exception.ResourceNotFoundException;
import core.framework.web.rest.exception.ErrorResponse;
import core.framework.web.rest.exception.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

/**
 * @author neo
 */
@ControllerAdvice
public class RESTErrorControllerAdvice {
    @Inject
    ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse notFound(ResourceNotFoundException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorResponse methodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse error(Throwable e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorResponse error(HttpMediaTypeNotSupportedException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }
}
