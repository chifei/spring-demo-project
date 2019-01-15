package core.framework.web.rest.client;

import core.framework.exception.InvalidRequestException;
import core.framework.exception.RemoteServiceException;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.http.HTTPResponse;
import core.framework.util.JSONBinder;
import core.framework.web.rest.exception.ErrorResponse;
import core.framework.web.rest.exception.FieldError;
import core.framework.web.rest.exception.ValidationErrorResponse;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author neo
 */
public class APIClientInterceptor implements MethodInterceptor {
    private final Logger logger = LoggerFactory.getLogger(APIClientInterceptor.class);

    String serviceURL;
    Map<Method, PathVariableProcessor> pathVariableProcessors;
    Map<Method, RequestBodyProcessor> requestBodyProcessors;
    Map<Method, QueryVariableProcessor> queryVariableProcessors;
    HTTPClient httpClient;
    RequestSigner requestSigner;

    @Override
    public Object intercept(Object object, Method method, Object[] params, MethodProxy proxy) throws Throwable {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping == null)
            return proxy.invokeSuper(object, params); // only intercept methods with @RequestMapping

        String url = url(method, params, requestMapping.value()[0]);

        HTTPRequest request = new HTTPRequest(url, httpMethod(requestMapping))
            .accept(ContentType.APPLICATION_JSON);

        RequestBodyProcessor requestBodyProcessor = requestBodyProcessors.get(method);
        if (requestBodyProcessor != null) {
            request.text(requestBodyProcessor.body(params), ContentType.APPLICATION_JSON);
        }
        if (requestSigner != null) requestSigner.sign(request);
        HTTPResponse response = httpClient.execute(request);

        validateResponse(response);

        return response(method, response.responseText());
    }

    private void validateResponse(HTTPResponse response) {
        int statusCode = response.statusCode();
        if (statusCode >= HttpStatus.OK.value() && statusCode <= HttpStatus.IM_USED.value()) return;
        try {
            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                ValidationErrorResponse validationErrorResponse = JSONBinder.fromJSON(ValidationErrorResponse.class, response.responseText());
                FieldError fieldError = validationErrorResponse.fieldErrors.get(0);
                throw new InvalidRequestException(fieldError.field, fieldError.message);
            } else {
                ErrorResponse errorResponse = JSONBinder.fromJSON(ErrorResponse.class, response.responseText());
                throw new RemoteServiceException(errorResponse.message, errorResponse.exceptionTrace);
            }
        } catch (RemoteServiceException | InvalidRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.warn("failed to decode response, statusCode={}, responseText={}", statusCode, response.responseText(), e);
            throw new RemoteServiceException("received non 2xx status code, status=" + statusCode + ", remoteMessage=" + response.responseText(), e);
        }
    }

    private HTTPMethod httpMethod(RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) return HTTPMethod.POST;
        return HTTPMethod.valueOf(requestMapping.method()[0].name());
    }

    private Object response(Method method, String responseBody) {
        Class<?> responseClass = method.getReturnType();
        if (Void.TYPE.equals(responseClass)) return null;
        return JSONBinder.fromJSON(responseClass, responseBody);
    }

    private String url(Method method, Object[] params, String urlPattern) {
        StringBuilder url = new StringBuilder().append(serviceURL);
        PathVariableProcessor pathVariableProcessor = pathVariableProcessors.get(method);
        QueryVariableProcessor queryVariableProcessor = queryVariableProcessors.get(method);

        if (pathVariableProcessor != null) {
            url.append(pathVariableProcessor.url(urlPattern, params));
        } else {
            url.append(urlPattern);
        }

        if (queryVariableProcessor != null) {
            url.append(queryVariableProcessor.urlParams(params));
        }

        return url.toString();
    }
}
