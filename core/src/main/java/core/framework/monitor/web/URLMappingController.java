package core.framework.monitor.web;

import core.framework.monitor.MonitorAccessControl;
import core.framework.monitor.view.URLMapping;
import core.framework.monitor.view.URLMappings;
import core.framework.web.request.RequestContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author neo
 */
@RestController
public class URLMappingController {
    @Inject
    RequestContext requestContext;
    @Inject
    RequestMappingHandlerMapping handlerMapping;

    @RequestMapping(value = "/monitor/url-mappings", method = RequestMethod.GET)
    @ResponseBody
    public URLMappings urlMappings() {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        URLMappings mappings = new URLMappings();
        Map<RequestMappingInfo, HandlerMethod> methods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methods.entrySet()) {
            URLMapping mapping = new URLMapping();
            RequestMappingInfo mappingInfo = entry.getKey();
            mapping.path = getPathInfo(mappingInfo);
            mapping.controllerClass = entry.getValue().getBeanType().getName();
            mapping.controllerMethod = getMethodInfo(entry.getValue().getMethod());
            mappings.mappings.add(mapping);
        }
        return mappings;
    }

    private String getMethodInfo(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(getTypeInfo(method.getGenericReturnType()))
            .append(' ')
            .append(method.getName())
            .append('(');
        int index = 0;
        for (Type paramType : method.getGenericParameterTypes()) {
            if (index > 0) builder.append(", ");
            builder.append(getTypeInfo(paramType));
            index++;
        }
        builder.append(')');
        return builder.toString();
    }

    private String getTypeInfo(Type type) {
        if (type instanceof ParameterizedType) {
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            StringBuilder builder = new StringBuilder().append(rawType.getSimpleName()).append("<");
            int index = 0;
            for (Type argumentType : ((ParameterizedType) type).getActualTypeArguments()) {
                if (index > 0) builder.append(", ");
                builder.append(getTypeInfo(argumentType));
                index++;
            }
            return builder.append(">").toString();
        } else if (type instanceof Class) {
            return ((Class) type).getSimpleName();
        } else {
            return type.toString();
        }
    }

    private String getPathInfo(RequestMappingInfo mappingInfo) {
        StringBuilder builder = new StringBuilder()
            .append(mappingInfo.getPatternsCondition())
            .append(", methods=").append(mappingInfo.getMethodsCondition());
        return builder.toString();
    }
}
