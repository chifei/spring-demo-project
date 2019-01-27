package app.demo.common.web.interceptor;


import app.demo.common.exception.ForbiddenException;
import app.demo.common.util.ControllerHelper;
import app.demo.common.web.UserInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PermissionRequiredInterceptor extends HandlerInterceptorAdapter {
    @Inject
    UserInfo userInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PermissionRequired permissionRequired = permissionRequired(handler);
        if (permissionRequired != null && !userInfo.hasPermission(permissionRequired.value())) {
            throw new ForbiddenException("You don't have permission");
        }
        return super.preHandle(request, response, handler);
    }

    private PermissionRequired permissionRequired(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, PermissionRequired.class);
    }
}
