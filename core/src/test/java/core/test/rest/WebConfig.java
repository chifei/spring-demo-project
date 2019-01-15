package core.test.rest;

import core.framework.task.TaskExecutor;
import core.framework.web.DefaultServiceConfig;
import org.springframework.context.annotation.Bean;
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
    @Bean
    public TaskExecutor taskExecutor() {
        return TaskExecutor.fixedSizeExecutor(1);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor());
    }
}
