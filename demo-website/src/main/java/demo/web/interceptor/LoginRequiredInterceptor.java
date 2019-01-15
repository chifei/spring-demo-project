package demo.web.interceptor;

import core.framework.web.site.ControllerHelper;
import demo.web.UserInfo;
import demo.web.exception.UnauthenticatedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chi
 */
public class LoginRequiredInterceptor extends HandlerInterceptorAdapter {
    @Inject
    UserInfo userInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isLoginRequired(handler) && !userInfo.isUserLogin()) {
            throw new UnauthenticatedException();
        }
        return super.preHandle(request, response, handler);
    }

    private boolean isLoginRequired(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, LoginRequired.class) != null;
    }
}
