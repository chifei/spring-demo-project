package demo.product;

import core.framework.cache.CacheRegistry;
import core.framework.cache.DefaultCacheConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author neo
 */
@Configuration
public class CacheConfig extends DefaultCacheConfig {
    @Override
    protected void addCaches(CacheRegistry registry) {
    }
}
