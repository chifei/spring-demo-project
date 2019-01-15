package core.framework.cache;

import core.framework.util.AssertUtils;
import core.framework.util.JSONBinder;
import core.framework.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.lang.reflect.Modifier;

/**
 * @author neo
 */
public class GuavaCache implements Cache, ManagedCache {
    private final Logger logger = LoggerFactory.getLogger(GuavaCache.class);

    private final String name;
    private final Class<?> objectType;

    private final com.google.common.cache.Cache<Object, Object> cache;

    public GuavaCache(String name, Class<?> objectType, com.google.common.cache.Cache<Object, Object> cache) {
        AssertUtils.assertNotNull(objectType, "objectType can not be null");
        AssertUtils.assertFalse(Modifier.isAbstract(objectType.getModifiers()) || Modifier.isInterface(objectType.getModifiers()), "objectType must be concrete class");
        this.name = name;
        this.objectType = objectType;
        this.cache = cache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        StopWatch watch = new StopWatch();
        boolean hit = false;
        try {
            String value = (String) cache.getIfPresent(String.valueOf(key));
            if (value == null) return null;
            hit = true;
            return new SimpleValueWrapper(JSONBinder.fromJSON(objectType, value));
        } catch (Throwable e) {
            logger.warn("failed to get, key={}", key, e);
            return null;
        } finally {
            logger.debug("get, key={}, hit={}, elapsedTime={}", key, hit, watch.elapsedTime());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper == null) return null;
        Object value = wrapper.get();
        if (type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        StopWatch watch = new StopWatch();
        AssertUtils.assertTrue(value == null || objectType.isAssignableFrom(value.getClass()), "value class does not match objectType, objectType={}, valueClass={}", objectType, value == null ? null : value.getClass());
        try {
            cache.put(String.valueOf(key), JSONBinder.toJSON(value));
        } catch (Throwable e) {
            logger.warn("failed to put, key={}", key, e);
        } finally {
            logger.debug("put, key={}, elapsedTime={}", key, watch.elapsedTime());
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
        try {
            cache.invalidate(String.valueOf(key));
        } catch (Throwable e) {
            logger.warn("failed to evict, key={}", key, e);
        } finally {
            logger.debug("evict, key={}, elapsedTime={}", key, watch.elapsedTime());
        }
    }

    @Override
    public void clear() {
        StopWatch watch = new StopWatch();
        try {
            cache.invalidateAll();
        } catch (Throwable e) {
            logger.warn("failed to clear", e);
        } finally {
            logger.debug("clear, cacheName={}, elapsedTime={}", name, watch.elapsedTime());
        }
    }

    @Override
    public Class<?> objectType() {
        return objectType;
    }
}
