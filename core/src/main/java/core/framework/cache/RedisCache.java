package core.framework.cache;

import core.framework.database.RedisAccess;
import core.framework.util.AssertUtils;
import core.framework.util.JSONBinder;
import core.framework.util.StopWatch;
import core.framework.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author neo
 */
public class RedisCache implements Cache, ManagedCache {
    private final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final String name;
    private final Class<?> objectType;
    private final TimeLength expirationTime;
    private final RedisAccess redisAccess;

    public RedisCache(String name, Class<?> objectType, TimeLength expirationTime, RedisAccess redisAccess) {
        AssertUtils.assertNotNull(objectType, "objectType can not be null");
        AssertUtils.assertFalse(Modifier.isAbstract(objectType.getModifiers()) || Modifier.isInterface(objectType.getModifiers()), "objectType must be concrete class");
        this.name = name;
        this.objectType = objectType;
        this.expirationTime = expirationTime;
        this.redisAccess = redisAccess;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return redisAccess;
    }

    @Override
    public ValueWrapper get(Object key) {
        StopWatch watch = new StopWatch();
        boolean hit = false;
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            String value = redisAccess.get(redisKey);
            if (value == null) return null;
            hit = true;
            return new SimpleValueWrapper(JSONBinder.fromJSON(objectType, value));
        } catch (Throwable e) {
            logger.warn("failed to get, redisKey={}", redisKey, e);
            return null;
        } finally {
            logger.debug("get, redisKey={}, hit={}, elapsedTime={}", redisKey, hit, watch.elapsedTime());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper element = get(key);
        Object value = element != null ? element.get() : null;
        if (type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    private String constructKey(Object key) {
        return name + ":" + key;
    }

    @Override
    public void put(Object key, Object value) {
        StopWatch watch = new StopWatch();
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            redisAccess.setExpire(redisKey, JSONBinder.toJSON(value), expirationTime);
        } catch (Throwable e) {
            logger.warn("failed to put, redisKey={}", redisKey, e);
        } finally {
            logger.debug("put, redisKey={}, elapsedTime={}", redisKey, watch.elapsedTime());
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper existingValue = get(key);
        if (existingValue == null) {
            put(key, value);
            return null;
        } else {
            return existingValue;
        }
    }

    @Override
    public void evict(Object key) {
        StopWatch watch = new StopWatch();
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            redisAccess.del(redisKey);
        } catch (Throwable e) {
            logger.warn("failed to evict, redisKey={}", redisKey, e);
        } finally {
            logger.debug("evict, redisKey={}, elapsedTime={}", redisKey, watch.elapsedTime());
        }
    }

    @Override
    public void clear() {
        StopWatch watch = new StopWatch();
        try {
            Set<String> keys = redisAccess.keys(name + ":*");
            deleteInBatch(keys, 128);
        } catch (Throwable e) {
            logger.warn("failed to clear, cacheName={}", name, e);
        } finally {
            logger.debug("clear, cacheName={}, elapsedTime={}", name, watch.elapsedTime());
        }
    }

    void deleteInBatch(Set<String> keys, int batchSize) {
        String[] batch = new String[batchSize];
        int i = 0;
        for (String key : keys) {
            batch[i] = key;
            i++;
            if (i >= batchSize) {
                redisAccess.del(batch);
                i = 0;
            }
        }
        if (i < batchSize) {
            String[] left = new String[i];
            System.arraycopy(batch, 0, left, 0, i);
            redisAccess.del(left);
        }
    }

    @Override
    public Class<?> objectType() {
        return objectType;
    }
}
