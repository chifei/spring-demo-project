package app.demo.common.web.interceptor;


import app.demo.common.util.ControllerHelper;
import app.demo.common.web.UserInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginRequiredInterceptor extends HandlerInterceptorAdapter {
    @Inject
    UserInfo userInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isLoginRequired(handler) && !userInfo.isUserLogin()) {
            response.sendRedirect("/login");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private boolean isLoginRequired(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, LoginRequired.class) != null;
    }
}
