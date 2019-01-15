package core.framework.web.site.cookie;

import core.framework.util.ReadOnly;
import core.framework.util.TimeLength;

/**
 * @author neo
 */
public final class CookieSpec<T> {
    private static final TimeLength MAX_AGE_SESSION_SCOPE = TimeLength.seconds(-1);
    private final String name;
    private final Class<T> type;
    private final ReadOnly<String> path = new ReadOnly<>();
    private final ReadOnly<Boolean> httpOnly = new ReadOnly<>();
    private final ReadOnly<Boolean> secure = new ReadOnly<>();
    private final ReadOnly<TimeLength> maxAge = new ReadOnly<>();

    private CookieSpec(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static CookieSpec<Integer> intKey(String name) {
        return new CookieSpec<>(name, Integer.class);
    }

    public static CookieSpec<String> stringKey(String name) {
        return new CookieSpec<>(name, String.class);
    }

    public String name() {
        return name;
    }

    public Class<T> type() {
        return type;
    }

    public String path() {
        return path.value();
    }

    public Boolean isHTTPOnly() {
        return httpOnly.value();
    }

    public Boolean isSecure() {
        return secure.value();
    }

    public TimeLength maxAge() {
        return maxAge.value();
    }

    public boolean pathAssigned() {
        return path.assigned();
    }

    public boolean httpOnlyAssigned() {
        return httpOnly.assigned();
    }

    public boolean secureAssigned() {
        return secure.assigned();
    }

    public boolean maxAgeAssigned() {
        return maxAge.assigned();
    }

    public CookieSpec<T> httpOnly() {
        httpOnly.set(true);
        return this;
    }

    public CookieSpec<T> path(String path) {
        this.path.set(path);
        return this;
    }

    public CookieSpec<T> secure() {
        secure.set(true);
        return this;
    }

    public CookieSpec<T> maxAge(TimeLength maxAge) {
        this.maxAge.set(maxAge);
        return this;
    }

    public CookieSpec<T> sessionScope() {
        maxAge.set(MAX_AGE_SESSION_SCOPE);
        return this;
    }
}
