package app.demo.common.web.interceptor;

import com.google.common.base.Strings;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocaleInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String language = request.getParameter("lang");
        if (!Strings.isNullOrEmpty(language)) {
            response.addCookie(new Cookie("lang", language));
        }
        return super.preHandle(request, response, handler);
    }
}
