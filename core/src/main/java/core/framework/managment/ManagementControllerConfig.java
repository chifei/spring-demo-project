package core.framework.managment;

import core.framework.managment.web.CacheManagementController;
import core.framework.managment.web.ServerStatusManagementController;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public class ManagementControllerConfig {
    @Bean
    public CacheManagementController cacheManagementController() {
        return new CacheManagementController();
    }

    @Bean
    public ServerStatusManagementController serverStatusManagementController() {
        return new ServerStatusManagementController();
    }
}
