package core.framework.web.filter;

import com.google.common.io.ByteStreams;
import core.framework.util.AssertUtils;
import core.framework.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author neo
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    static final String HEADER_X_FORWARDED_PROTO = "x-forwarded-proto";
    private String scheme;
    private ServletInputStream inputStream;
    private String body;
    private BufferedReader reader;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void initialize() throws IOException {
        HttpServletRequest request = (HttpServletRequest) getRequest();

        preLoadBody(request);
        scheme = parseProxyScheme(request);
    }

    private void preLoadBody(HttpServletRequest request) throws IOException {
        if (preLoadBody()) {
            Charset charset = Charset.forName(getCharacterEncoding());
            ServletInputStream originalInputStream = request.getInputStream();
            AssertUtils.assertNotNull(originalInputStream, "POST or PUT should have input steam as body");
            byte[] bodyBytes = ByteStreams.toByteArray(originalInputStream);
            body = new String(bodyBytes, charset);
            inputStream = new RequestCachingInputStream(body.getBytes(charset));
        }
    }

    private String parseProxyScheme(HttpServletRequest originalRequest) {
        String forwardedProtocol = originalRequest.getHeader(HEADER_X_FORWARDED_PROTO);
        if (StringUtils.hasText(forwardedProtocol)) {
            return forwardedProtocol.toLowerCase();
        }
        return originalRequest.getScheme();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null)
            return inputStream;
        return super.getInputStream();
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public boolean isSecure() {
        return "https".equals(scheme);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return reader;
    }

    public String body() {
        return body;
    }

    final boolean isForm() {
        String contentType = getContentType();
        if (contentType == null) return false;  // invalid request may not pass content type header
        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            return mediaType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA) || mediaType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED);
        } catch (InvalidMediaTypeException e) {
            return false;
        }
    }

    final boolean containsBody() {
        String originalMethod = getMethod();
        return HttpMethod.POST.name().equals(originalMethod) || HttpMethod.PUT.name().equals(originalMethod);
    }

    public String relativeURLWithQueryString() {
        String path = requestServletPath();
        String queryString = requestQueryString();
        if (StringUtils.hasText(queryString)) {
            return path + '?' + queryString;
        } else {
            return path;
        }
    }

    public String relativeURL() {
        return requestServletPath();
    }

    // path info starts with '/' and doesn't include any context (servletContext or deploymentContext)
    private String requestServletPath() {
        String forwardPath = (String) getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH);
        if (forwardPath != null)
            return forwardPath;
        return getServletPath();
    }

    public String requestQueryString() {
        String forwardQueryString = (String) getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
        if (forwardQueryString != null)
            return forwardQueryString;
        return getQueryString();
    }

    public String clientIP() {
        return RemoteAddress.create(this).clientIP();
    }

    final boolean preLoadBody() {
        return containsBody() && !isForm();
    }
}
