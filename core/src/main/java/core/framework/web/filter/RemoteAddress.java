package core.framework.web.filter;


import core.framework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author neo
 */
public class RemoteAddress {
    static final String HTTP_HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    private final String remoteAddress;
    // for original ip if there is proxy
    private final String xForwardedFor;

    RemoteAddress(String remoteAddress, String xForwardedFor) {
        this.remoteAddress = remoteAddress;
        this.xForwardedFor = xForwardedFor;
    }

    public static RemoteAddress create(HttpServletRequest request) {
        String directRemoteAddress = request.getRemoteAddr();
        String xForwardedFor = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        return new RemoteAddress(directRemoteAddress, xForwardedFor);
    }

    public String remoteAddress() {
        return remoteAddress;
    }

    public String xForwardedFor() {
        return xForwardedFor;
    }

    /**
     * get actual client ip, being aware of proxy
     *
     * @return the ip of client from request
     */
    public String clientIP() {
        if (!StringUtils.hasText(xForwardedFor))
            return remoteAddress;
        int index = xForwardedFor.indexOf(',');
        if (index > 0)
            return xForwardedFor.substring(0, index);
        return xForwardedFor;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(xForwardedFor)) {
            builder.append(xForwardedFor).append(", ");
        }
        builder.append(remoteAddress);
        return builder.toString();
    }
}
