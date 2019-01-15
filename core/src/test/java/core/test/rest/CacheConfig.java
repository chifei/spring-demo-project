package core.test.rest;

import core.framework.cache.CacheRegistry;
import core.framework.cache.DefaultCacheConfig;
import core.framework.util.TimeLength;
import core.test.rest.cache.CacheTestRepository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author neo
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig extends DefaultCacheConfig {
    @Override
    protected void addCaches(CacheRegistry cacheRegistry) {
        cacheRegistry.add(CacheTestRepository.CACHE_NAME, CacheTestRepository.Entity.class, TimeLength.hours(2));
    }
}
