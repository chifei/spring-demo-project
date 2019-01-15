package core.test.rest.cache;

import core.test.rest.SpringTest;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author neo
 */
public class CacheTest extends SpringTest {
    @Inject
    CacheTestRepository repository;
    @Inject
    CacheManager cacheManager;

    // due to cache is global, unit tests run in multiple threads, test one cache in same method
    @Test
    public void retrieveAndEvict() {
        String key = "key";

        repository.resetMethodCalled();
        CacheTestRepository.Entity entity1 = repository.getEntityById(key);
        CacheTestRepository.Entity entity2 = repository.getEntityById(key);

        assertNotSame(entity1, entity2);
        assertEquals(entity1.getId(), entity2.getId());
        assertEquals("should be called once", 1, repository.getMethodCalled());

        repository.resetMethodCalled();
        cacheManager.getCache(CacheTestRepository.CACHE_NAME).clear();

        entity1 = repository.getEntityById(key);
        repository.evictEntityById(key);
        entity2 = repository.getEntityById(key);

        assertNotSame(entity1, entity2);
        assertEquals(entity1.getId(), entity2.getId());
        assertEquals("should be called 3 times", 3, repository.getMethodCalled());
    }

    @Test
    public void retrieveAndEvictWithDefaultKey() {
        repository.resetMethodCalled();
        CacheTestRepository.Entity entity1 = repository.getDefaultEntity();
        CacheTestRepository.Entity entity2 = repository.getDefaultEntity();

        assertEquals(entity1.getId(), entity2.getId());
        assertEquals("should be called once", 1, repository.getMethodCalled());

        repository.resetMethodCalled();
        cacheManager.getCache(CacheTestRepository.CACHE_NAME).clear();

        entity1 = repository.getDefaultEntity();
        repository.evictDefaultEntity();
        entity2 = repository.getDefaultEntity();

        assertEquals(entity1.getId(), entity2.getId());
        assertEquals("should be called 3 times", 3, repository.getMethodCalled());
    }
}
