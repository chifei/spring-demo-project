package core.framework.web.filter;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author neo
 */
public class RequestWrapperTest {
    @Test
    public void forwardProtocol() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/");
        request.addHeader(RequestWrapper.HEADER_X_FORWARDED_PROTO, "https");

        RequestWrapper wrapper = new RequestWrapper(request);
        wrapper.initialize();

        assertEquals("https", wrapper.getScheme());
        assertTrue(wrapper.isSecure());
    }

    @Test
    public void isForm() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "http://localhost/");
        RequestWrapper wrapper = new RequestWrapper(request);

        request.setContentType("application/x-www-form-urlencoded");
        assertTrue(wrapper.isForm());

        request.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        assertTrue(wrapper.isForm());
    }
}
