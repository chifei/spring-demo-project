package app.demo;

import com.google.common.collect.Lists;
import app.demo.user.service.Caches;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chi
 */
@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Lists.newArrayList(
            new ConcurrentMapCache(Caches.CACHE_USER_INFO))
        );
        return cacheManager;
    }
}
