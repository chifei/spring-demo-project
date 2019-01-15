package core.framework.web.rest;

import core.framework.exception.ForbiddenException;
import core.framework.exception.InvalidRequestException;
import core.framework.exception.ResourceNotFoundException;
import core.framework.exception.UserAuthorizationException;
import core.framework.web.i18n.Messages;
import core.framework.web.rest.exception.ErrorResponse;
import core.framework.web.rest.exception.ErrorResponseBuilder;
import core.framework.web.rest.exception.FieldError;
import core.framework.web.rest.exception.ValidationErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Locale;

/**
 * @author neo
 */
@ControllerAdvice(annotations = RestController.class)
public class RESTControllerAdvice {
    @Inject
    ErrorResponseBuilder errorResponseBuilder;
    @Inject
    Messages messages;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse error(Throwable e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse notFound(ResourceNotFoundException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(UserAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse unauthorized(UserAuthorizationException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse forbidden(ForbiddenException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(HttpMessageNotReadableException e) {
        return createValidationResponse(e);
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(TypeMismatchException e) {
        return createValidationResponse(e);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(BindException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(MethodArgumentNotValidException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(InvalidRequestException e) {
        Locale locale = LocaleContextHolder.getLocale();
        ValidationErrorResponse response = new ValidationErrorResponse();
        FieldError error = new FieldError();
        error.field = e.field();
        error.message = messages.getMessage(e.getMessage(), locale);
        response.fieldErrors.add(error);
        return response;
    }

    private ValidationErrorResponse createValidationResponse(Exception e) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        FieldError error = new FieldError();
        error.message = e.getMessage();
        response.fieldErrors.add(error);
        return response;
    }

    private ValidationErrorResponse createValidationResponse(BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        ValidationErrorResponse response = new ValidationErrorResponse();
        for (org.springframework.validation.FieldError fieldError : bindingResult.getFieldErrors()) {
            FieldError error = new FieldError();
            error.field = fieldError.getField();
            error.message = messages.getMessage(fieldError, locale);
            response.fieldErrors.add(error);
        }
        return response;
    }
}
