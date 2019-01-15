package core.test.rest.cache;

import org.springframework.stereotype.Repository;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;

/**
 * @author neo
 */
@Repository
public class CacheTestRepository {
    public static final String CACHE_NAME = "entities";

    final ThreadLocal<Integer> methodCalled = new ThreadLocal<>();

    @CacheResult(cacheName = CACHE_NAME)
    public Entity getEntityById(@CacheKey String id) {
        int count = methodCalled.get();
        methodCalled.set(count + 1);
        Entity entity = new Entity();
        entity.setId(id);
        return entity;
    }

    @CacheRemove(cacheName = CACHE_NAME)
    public void evictEntityById(@CacheKey String id) {
        int count = methodCalled.get();
        methodCalled.set(count + 1);
    }

    @CacheResult(cacheName = CACHE_NAME)
    public Entity getDefaultEntity() {
        int count = methodCalled.get();
        methodCalled.set(count + 1);
        Entity entity = new Entity();
        entity.setId("default");
        return entity;
    }

    @CacheRemove(cacheName = CACHE_NAME)
    public void evictDefaultEntity() {
        int count = methodCalled.get();
        methodCalled.set(count + 1);
    }

    int getMethodCalled() {
        return methodCalled.get();
    }

    void resetMethodCalled() {
        methodCalled.set(0);
    }

    public static class Entity {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
