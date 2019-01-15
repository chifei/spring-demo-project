package core.framework.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
@EnableCaching(proxyTargetClass = true)
public abstract class DefaultCacheConfig extends CachingConfigurerSupport {
    @Bean
    public CacheSettings cacheSettings() {
        return new CacheSettings();
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        CacheManager cacheManager = createCacheManager();
        addCaches((CacheRegistry) cacheManager);
        return cacheManager;
    }

    private CacheManager createCacheManager() {
        CacheSettings settings = cacheSettings();
        CacheProvider provider = settings.cacheProvider();
        if (CacheProvider.REDIS.equals(provider)) {
            return new RedisCacheManager(settings.remoteCacheServer());
        } else if (CacheProvider.LOCAL.equals(provider)) {
            return new GuavaCacheManager();
        }
        throw new IllegalStateException("not supported cache provider, provider=" + provider);
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new DefaultCacheKeyGenerator();
    }

    protected abstract void addCaches(CacheRegistry registry);
}
