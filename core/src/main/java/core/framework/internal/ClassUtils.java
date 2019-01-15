package core.framework.internal;

/**
 * @author neo
 */
public final class ClassUtils {
    public static String getOriginalClassName(Class<?> targetClass) {
        return getOriginalClass(targetClass).getName();
    }

    public static Class<?> getOriginalClass(Class<?> targetClass) {
        String className = targetClass.getName();
        if (className.contains("$$EnhancerBySpringCGLIB")) {
            return targetClass.getSuperclass();
        }
        return targetClass;
    }

    public static String getOriginalClassName(Object object) {
        return getOriginalClassName(object.getClass());
    }

    public static String getSimpleOriginalClassName(Object object) {
        String fullClassName = getOriginalClassName(object);
        int lastNamespace = fullClassName.lastIndexOf('.');
        if (lastNamespace > -1) {
            return fullClassName.substring(lastNamespace + 1);
        }
        return fullClassName;
    }
}
