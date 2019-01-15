package core.framework.web.request;

import core.framework.util.ReadOnly;
import core.framework.web.filter.RequestWrapper;

import java.util.Date;

/**
 * @author neo
 */
public class RequestContextImpl implements RequestContext {
    private final ReadOnly<RequestWrapper> request = new ReadOnly<>();
    private final ReadOnly<String> requestId = new ReadOnly<>();
    private final ReadOnly<Date> requestDate = new ReadOnly<>();

    @Override
    public String serverName() {
        return request.value().getServerName();
    }

    @Override
    public boolean secure() {
        return request.value().isSecure();
    }

    @Override
    public String clientIP() {
        return request.value().clientIP();
    }

    @Override
    public String requestId() {
        return requestId.value();
    }

    @Override
    public Date requestDate() {
        return requestDate.value();
    }

    @Override
    public String header(String name) {
        return request.value().getHeader(name);
    }

    @Override
    public String relativeURLWithQueryString() {
        return request.value().relativeURLWithQueryString();
    }

    @Override
    public String relativeURL() {
        return request.value().relativeURL();
    }

    @Override
    public String fullURLWithQueryString() {
        RequestWrapper request = this.request.value();

        String scheme = request.getScheme();
        int port = request.getServerPort();

        StringBuilder builder = new StringBuilder();
        builder.append(scheme).append("://").append(request.getServerName());
        if (("https".equals(scheme) && port != 443)
            || ("http".equals(scheme) && port != 80)) {
            builder.append(':').append(port);
        }

        builder.append(request.relativeURLWithQueryString());

        return builder.toString();
    }

    public void setRequestId(String requestId) {
        this.requestId.set(requestId);
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate.set(requestDate);
    }

    public void setHTTPRequest(RequestWrapper request) {
        this.request.set(request);
    }
}
