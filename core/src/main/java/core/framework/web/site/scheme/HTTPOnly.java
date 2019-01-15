package core.framework.web.site.scheme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Underlying URL will only be handled in HTTP, redirect to HTTP if not match
 * Put to Controller class level or method level, Class level annotation takes precedence
 *
 * @author neo
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTPOnly {
}
