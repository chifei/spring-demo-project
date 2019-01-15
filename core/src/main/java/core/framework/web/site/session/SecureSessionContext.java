package core.framework.web.site.session;


import core.framework.util.AssertUtils;

/**
 * @author neo
 */
public class SecureSessionContext extends SessionContext {
    private boolean underSecureRequest;

    @Override
    public <T> T get(SessionKey<T> key) {
        AssertUtils.assertTrue(underSecureRequest, "secure session can only be used under https");
        return super.get(key);
    }

    @Override
    public <T> void set(SessionKey<T> key, T value) {
        AssertUtils.assertTrue(underSecureRequest, "secure session can only be used under https");
        super.set(key, value);
    }

    void underSecureRequest() {
        underSecureRequest = true;
    }
}
