package core.framework.web.request;

import java.util.Date;

/**
 * @author neo
 */
public interface RequestContext {
    String serverName();

    boolean secure();

    String clientIP();

    String requestId();

    Date requestDate();

    String header(String name);

    String relativeURLWithQueryString();

    String relativeURL();

    String fullURLWithQueryString();
}
