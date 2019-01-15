package demo.customer;

import core.framework.web.DefaultServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @author neo
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class WebConfig extends DefaultServiceConfig {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor());
    }
}
