package core.framework.web.site.session;


import core.framework.util.JSONBinder;
import core.framework.util.ReadOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 */
public class SessionContext {
    private final ReadOnly<String> id = new ReadOnly<>();
    private final Map<String, String> session = new HashMap<>();
    private boolean changed;
    private boolean invalidated;

    public <T> T get(SessionKey<T> key) {
        String value = session.get(key.name());
        if (value == null) return null;
        return JSONBinder.fromJSON(key.type(), value);
    }

    public <T> void set(SessionKey<T> key, T value) {
        session.put(key.name(), JSONBinder.toJSON(value));
        changed = true;
    }

    public void invalidate() {
        session.clear();
        changed = true;
        invalidated = true;
    }

    boolean changed() {
        return changed;
    }

    boolean invalidated() {
        return invalidated;
    }

    String id() {
        return id.value();
    }

    void setId(String id) {
        this.id.set(id);
    }

    void loadSessionData(Map<String, String> sessionData) {
        session.putAll(sessionData);
    }

    Map<String, String> sessionData() {
        return session;
    }

    void saved() {
        changed = false;
    }

    void requireNewSessionId() {
        changed = true;
    }
}
