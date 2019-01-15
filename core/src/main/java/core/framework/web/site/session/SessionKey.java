package core.framework.web.site.session;

import com.google.common.base.MoreObjects;
import core.framework.util.StringUtils;

import java.util.Date;

/**
 * @author neo
 */
public class SessionKey<T> {
    static final String ERROR_MESSAGE_NAME_CANNOT_BE_EMPTY = "name cannot be empty";
    static final String ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_NULL = "type cannot be null";
    static final String ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE = "type cannot be primitive, use wrapper class instead, e.g. Integer.class for int.class";
    static final String ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_INTERFACE = "type cannot be interface, use concrete class instead with JAXB annotation";

    final String name;
    final Class<? extends T> type;

    SessionKey(String name, Class<? extends T> type) {
        if (!StringUtils.hasText(name)) throw new IllegalArgumentException(ERROR_MESSAGE_NAME_CANNOT_BE_EMPTY);
        if (type == null) throw new IllegalArgumentException(ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_NULL);
        if (type.isPrimitive()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE);
        }

        if (type.isInterface()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_INTERFACE);
        }

        this.name = name;
        this.type = type;
    }

    public static <T> SessionKey<T> key(String name, Class<T> targetClass) {
        return new SessionKey<>(name, targetClass);
    }

    public static SessionKey<Integer> intKey(String name) {
        return key(name, Integer.class);
    }

    public static SessionKey<Long> longKey(String name) {
        return key(name, Long.class);
    }

    public static SessionKey<Double> doubleKey(String name) {
        return key(name, Double.class);
    }

    public static SessionKey<String> stringKey(String name) {
        return key(name, String.class);
    }

    public static SessionKey<Date> dateKey(String name) {
        return key(name, Date.class);
    }

    public static SessionKey<Boolean> booleanKey(String name) {
        return key(name, Boolean.class);
    }

    public String name() {
        return name;
    }

    public Class<? extends T> type() {
        return type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("type", type)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionKey)) return false;

        SessionKey that = (SessionKey) o;

        return name.equals(that.name)
            && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}