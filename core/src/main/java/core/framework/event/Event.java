package core.framework.event;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author neo
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Event {
    String value();
}
