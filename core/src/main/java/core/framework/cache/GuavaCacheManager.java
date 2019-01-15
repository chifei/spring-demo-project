package core.framework.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import core.framework.util.TimeLength;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
public class GuavaCacheManager extends AbstractCacheManager implements CacheRegistry {
    public static final int MAX_SIZE = 10000;   // for local cache, make max size constant, use remote cache for critical system

    private final List<GuavaCache> caches = Lists.newArrayList();

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    @Override
    public void add(String cacheName, Class<?> objectType, TimeLength expirationTime) {
        com.google.common.cache.Cache<Object, Object> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expirationTime.toMilliseconds(), TimeUnit.MILLISECONDS)
            .maximumSize(MAX_SIZE)
            .build();
        caches.add(new GuavaCache(cacheName, objectType, cache));
    }
}
