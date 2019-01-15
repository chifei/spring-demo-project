package core.framework.web.site.cookie;

import core.framework.web.site.session.RequireSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

/**
 * @author neo
 */
public class CookieInterceptorTest {
    CookieInterceptor interceptor;

    @Before
    public void createCookieInterceptor() {
        interceptor = new CookieInterceptor();
    }

    @Test
    public void sessionRequiresCookieSupport() throws NoSuchMethodException {
        HandlerMethod handler = new HandlerMethod(new TestController(), TestController.class.getMethod("execute"));
        Assert.assertTrue("@RequireSession requires cookies", interceptor.requireCookie(handler));
    }

    public static class TestController {
        @RequireSession
        public String execute() {
            return "view";
        }
    }
}
