package core.framework.web;

import core.framework.web.rest.RESTErrorControllerAdvice;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public abstract class DefaultServiceConfig extends AbstractWebConfig {
    @Bean
    RESTErrorControllerAdvice restErrorControllerAdvice() {
        return new RESTErrorControllerAdvice();
    }
}
