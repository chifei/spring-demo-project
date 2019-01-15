package core.framework.web.site.cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.verify;

/**
 * @author neo
 */
public class CookieContextTest {
    CookieContext cookieContext;
    Cookie cookie;

    @Before
    public void prepare() {
        cookieContext = new CookieContext();
        cookieContext.setHttpServletResponse(new MockHttpServletResponse());
        cookie = Mockito.mock(Cookie.class);
    }

    @Test
    public void setCookieHTTPOnly() {
        CookieSpec<String> spec = CookieSpec.stringKey("test").httpOnly();

        cookieContext.setCookieAttributes(spec, cookie);

        verify(cookie).setHttpOnly(true);
    }

    @Test
    public void setCookie() {
        String value = "setCookieValue";
        CookieSpec<String> spec = CookieSpec.stringKey("setCookieName").httpOnly();
        cookieContext.setCookie(spec, value);
        Assert.assertEquals(value, cookieContext.getCookie(spec));
    }
}
