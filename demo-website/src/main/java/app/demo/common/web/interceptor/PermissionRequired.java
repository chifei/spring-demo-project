package app.demo.common.web.interceptor;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Retention(RUNTIME)
public @interface PermissionRequired {
    String[] value();
}
