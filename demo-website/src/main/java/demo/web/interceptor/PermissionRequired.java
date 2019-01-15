package demo.web.interceptor;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author chi
 */
@Retention(RUNTIME)
public @interface PermissionRequired {
    String value();
}
