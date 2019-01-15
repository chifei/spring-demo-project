package core.framework.web.rest.client;

import core.framework.http.HTTPRequest;

/**
 * @author neo
 */
public interface RequestSigner {
    void sign(HTTPRequest request);
}
