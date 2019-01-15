package core.framework.web.site.form;

import core.framework.exception.InvalidRequestException;
import core.framework.util.JSONBinder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author neo
 */
public class FormArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return paramType.isAnnotationPresent(XmlAccessorType.class)
            || paramType.isAnnotationPresent(XmlType.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            Class<?> paramType = parameter.getParameterType();
            Map<String, String> params = new HashMap<>();
            Iterator<String> names = webRequest.getParameterNames();
            while (names.hasNext()) {
                String name = names.next();
                params.put(name, webRequest.getParameter(name));
            }
            String paramJSON = JSONBinder.toJSON(params);
            return JSONBinder.fromJSON(paramType, paramJSON);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage(), e);
        }
    }
}
