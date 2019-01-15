package core.framework.monitor;

import core.framework.monitor.web.HealthCheckController;
import core.framework.monitor.web.MemoryUsageController;
import core.framework.monitor.web.ThreadInfoController;
import core.framework.monitor.web.URLMappingController;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public class MonitorControllerConfig {
    @Bean
    public HealthCheckController healthCheckController() {
        return new HealthCheckController();
    }

    @Bean
    public URLMappingController urlMappingController() {
        return new URLMappingController();
    }

    @Bean
    public ThreadInfoController threadInfoController() {
        return new ThreadInfoController();
    }

    @Bean
    public MemoryUsageController memoryUsageController() {
        return new MemoryUsageController();
    }
}
