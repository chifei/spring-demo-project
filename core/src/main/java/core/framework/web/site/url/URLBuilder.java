package core.framework.web.site.url;


import core.framework.util.AssertUtils;
import core.framework.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide standard way to construct url
 *
 * @author neo
 */
public final class URLBuilder {
    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String contextPath;
    private String logicalURL;
    private List<URLParam> params;

    /**
     * build relative URL starts with root, this relative url is for browser, it appends context to match the actual URL used by browser
     *
     * @return relative url valid for browser
     */
    public String buildRelativeURL() {
        StringBuilder builder = new StringBuilder();
        buildRelativeURLPart(builder, logicalURL);
        buildParameterPart(builder);
        return builder.toString();
    }

    public String buildFullURL() {
        AssertUtils.assertHasText(scheme, "scheme is required to build full url");
        AssertUtils.assertHasText(serverName, "serverName is required to build full url");

        StringBuilder builder = new StringBuilder();
        buildURLPrefix(builder);
        if (logicalURL != null && !logicalURL.startsWith("/"))
            buildRelativeURLPart(builder, "/" + logicalURL);
        else
            buildRelativeURLPart(builder, this.logicalURL);
        buildParameterPart(builder);
        return builder.toString();
    }

    private void buildParameterPart(StringBuilder builder) {
        if (params == null) return;
        char connector = logicalURL.indexOf('?') < 0 ? '?' : '&';
        builder.append(connector);
        boolean first = true;
        for (URLParam param : params) {
            if (!first) builder.append('&');
            builder.append(EncodingUtils.url(param.getName()))
                .append('=')
                .append(EncodingUtils.url(param.getValue()));
            first = false;
        }
    }

    void buildRelativeURLPart(StringBuilder builder, String logicalURL) {
        if (null != logicalURL && logicalURL.startsWith("/")) {
            String context = contextPath();
            builder.append(context).append(logicalURL);
        } else {
            builder.append(logicalURL == null ? "" : logicalURL);
        }
    }

    private String contextPath() {
        AssertUtils.assertNotNull(contextPath, "contextPath is required");
        if ("/".equals(contextPath)) return ""; // because the url value contains '/' already
        return contextPath;
    }

    private void buildURLPrefix(StringBuilder builder) {
        builder.append(scheme)
            .append("://")
            .append(serverName);

        int port = getTargetServerPort();

        if (!isDefaultPort(port)) {
            builder.append(':').append(port);
        }
    }

    private boolean isDefaultPort(int port) {
        return "http".equals(scheme) && port == 80
            || "https".equals(scheme) && port == 443;
    }

    int getTargetServerPort() {
        if (serverPort != null) return serverPort;
        if ("http".equals(scheme)) return 80;
        if ("https".equals(scheme)) return 443;
        throw new IllegalStateException("unknown scheme, scheme=" + scheme);
    }

    public void addParam(String name, String value) {
        if (params == null) params = new ArrayList<>();
        params.add(new URLParam(name, value));
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme.toLowerCase();
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setLogicalURL(String logicalURL) {
        this.logicalURL = logicalURL;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }
}
