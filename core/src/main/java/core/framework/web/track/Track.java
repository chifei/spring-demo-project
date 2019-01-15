package core.framework.web.track;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author neo
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface Track {
    int warningThresholdInMs() default -1;
}
