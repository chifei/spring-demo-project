package core.framework.cache;

import core.framework.database.RedisAccess;
import core.framework.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author neo
 */
public class RedisCacheManager extends AbstractCacheManager implements CacheRegistry {
    private static final TimeLength REDIS_TIME_OUT = TimeLength.seconds(2);
    private final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
    private final List<RedisCache> caches = new ArrayList<>();
    private final JedisPool redisPool;
    private final RedisAccess redisAccess;

    public RedisCacheManager(String remoteServer) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(REDIS_TIME_OUT.toMilliseconds());
        redisAccess = new RedisAccess();
        redisPool = new JedisPool(config, remoteServer);
        redisAccess.setRedisPool(redisPool);
    }

    @PreDestroy
    public void shutdown() {
        logger.info("shutdown redis pool");
        redisPool.destroy();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    public void add(String cacheName, Class<?> objectType, TimeLength expirationTime) {
        RedisCache cache = new RedisCache(cacheName, objectType, expirationTime, redisAccess);
        caches.add(cache);
    }
}
