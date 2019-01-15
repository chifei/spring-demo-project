package core.framework.web.site.session.provider;

import java.util.Map;

/**
 * @author neo
 */
public interface SessionProvider {
    Map<String, String> getAndRefresh(String sessionId);

    void save(String sessionId, Map<String, String> sessionData);

    void clear(String sessionId);
}
