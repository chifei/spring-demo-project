package core.framework.util;

import org.slf4j.helpers.MessageFormatter;

import java.util.regex.Pattern;

/**
 * support slf4j message format with params, e.g. "field={}"
 *
 * @author neo
 */
public final class AssertUtils {
    private AssertUtils() {
    }

    public static void assertNull(Object target, String message, Object... params) {
        if (target != null) {
            throwAssertionException(message, params);
        }
    }

    public static void assertNotNull(Object target, String message, Object... params) {
        if (target == null) {
            throwAssertionException(message, params);
        }
    }

    public static void assertTrue(boolean target, String message, Object... params) {
        if (!target) {
            throwAssertionException(message, params);
        }
    }

    public static void assertFalse(boolean target, String message, Object... params) {
        assertTrue(!target, message, params);
    }

    public static void assertHasText(String target, String message, Object... params) {
        if (!StringUtils.hasText(target)) {
            throwAssertionException(message, params);
        }
    }

    public static void assertMatches(String target, Pattern pattern, String message, Object... params) {
        if (!pattern.matcher(target).matches()) {
            throwAssertionException(message, params);
        }
    }

    private static void throwAssertionException(String message, Object[] params) {
        String errorMessage = params == null ? message : MessageFormatter.arrayFormat(message, params).getMessage();
        throw new AssertionException(errorMessage);
    }

    public static class AssertionException extends RuntimeException {
        private static final long serialVersionUID = -1023297548643274521L;

        public AssertionException(String message) {
            super(message);
        }
    }
}
