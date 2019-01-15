package core.framework.web.rest.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.framework.http.HTTPClient;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
public class APIClientBuilder {
    final String serviceURL;
    final HTTPClient httpClient;
    RequestSigner requestSigner;

    public APIClientBuilder(String serviceURL, HTTPClient httpClient) {
        this.serviceURL = serviceURL;
        this.httpClient = httpClient;
    }

    public APIClientBuilder signBy(RequestSigner requestSigner) {
        this.requestSigner = requestSigner;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> serviceClass) {
        validateServiceClass(serviceClass);

        Map<Method, PathVariableProcessor> pathVariableProcessors = Maps.newHashMap();
        Map<Method, RequestBodyProcessor> requestBodyProcessors = Maps.newHashMap();
        Map<Method, QueryVariableProcessor> queryVariableProcessors = Maps.newHashMap();

        for (Method method : serviceClass.getMethods()) {
            List<VariablePosition> pathVariablePositions = Lists.newArrayList();
            List<VariablePosition> queryVariablePositions = Lists.newArrayList();

            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0, length = annotations.length; i < length; i++) {
                Annotation[] paramAnnotations = annotations[i];
                PathVariable pathVariable = findPathVariable(paramAnnotations);
                if (pathVariable != null) {
                    pathVariablePositions.add(new VariablePosition(i, pathVariable.value()));
                }
                RequestBody requestBody = findRequestBody(paramAnnotations);
                if (requestBody != null) {
                    requestBodyProcessors.put(method, new RequestBodyProcessor(i));
                }
                RequestParam requestParam = findRequestParam(paramAnnotations);
                if (requestParam != null) {
                    queryVariablePositions.add(new VariablePosition(i, requestParam.value()));
                }
            }

            if (!pathVariablePositions.isEmpty())
                pathVariableProcessors.put(method, new PathVariableProcessor(pathVariablePositions));

            if (!queryVariablePositions.isEmpty())
                queryVariableProcessors.put(method, new QueryVariableProcessor(queryVariablePositions));
        }

        APIClientInterceptor interceptor = new APIClientInterceptor();
        interceptor.serviceURL = serviceURL;
        interceptor.pathVariableProcessors = pathVariableProcessors;
        interceptor.requestBodyProcessors = requestBodyProcessors;
        interceptor.queryVariableProcessors = queryVariableProcessors;
        interceptor.httpClient = httpClient;
        interceptor.requestSigner = requestSigner;
        return (T) Enhancer.create(serviceClass, interceptor);
    }

    private PathVariable findPathVariable(Annotation[] paramAnnotations) {
        for (Annotation annotation : paramAnnotations) {
            if (PathVariable.class.equals(annotation.annotationType())) return (PathVariable) annotation;
        }
        return null;
    }

    private RequestParam findRequestParam(Annotation[] paramAnnotations) {
        for (Annotation annotation : paramAnnotations) {
            if (RequestParam.class.equals(annotation.annotationType())) return (RequestParam) annotation;
        }
        return null;
    }

    private RequestBody findRequestBody(Annotation[] paramAnnotations) {
        for (Annotation annotation : paramAnnotations) {
            if (RequestBody.class.equals(annotation.annotationType())) return (RequestBody) annotation;
        }
        return null;
    }

    private <T> void validateServiceClass(Class<T> serviceClass) {
        Preconditions.checkArgument(serviceClass.isInterface(), "serviceClass must be interface");
        for (Method method : serviceClass.getMethods()) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            Preconditions.checkNotNull(requestMapping, "@RequestMapping is required, method=%s", method);
        }
    }
}
