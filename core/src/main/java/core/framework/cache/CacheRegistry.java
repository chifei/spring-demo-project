package core.framework.cache;

import core.framework.util.TimeLength;

/**
 * @author neo
 */
public interface CacheRegistry {
    void add(String cacheName, Class<?> objectType, TimeLength expirationTime);
}
