package core.framework.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author walter
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface Ignore {
}
