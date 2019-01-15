package core.framework.managment.web;

import core.framework.cache.DefaultCacheKeyGenerator;
import core.framework.exception.InvalidRequestException;
import core.framework.exception.ResourceNotFoundException;
import core.framework.internal.SpringObjectFactory;
import core.framework.monitor.MonitorAccessControl;
import core.framework.web.request.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
@RestController
public class CacheManagementController {
    private final Logger logger = LoggerFactory.getLogger(CacheManagementController.class);
    @Inject
    SpringObjectFactory springObjectFactory;
    @Inject
    RequestContext requestContext;

    @RequestMapping(value = "/management/cache/groups", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<String> getCacheGroups() {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        CacheManager cacheManager = cacheManager();
        return new ArrayList<>(cacheManager.getCacheNames());
    }

    @RequestMapping(value = "/management/cache/group/{group}", method = RequestMethod.DELETE)
    public String refreshCacheGroup(@PathVariable("group") String group) {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        Cache cache = cacheGroup(group);
        cache.clear();
        logger.info("refresh cache group, group={}, updatedBy={}", group, requestContext.clientIP());
        return String.format("cache group cleared, group=%s", group);
    }

    // only support json because XMLBinder doesn't support dynamic Object field type
    @RequestMapping(value = "/management/cache/group/{group}/key/{key}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Object getCacheItem(@PathVariable("group") String group, @PathVariable("key") String key) {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        String encodedKey = convertCacheKey(key);
        Cache cache = cacheGroup(group);
        return cache.get(encodedKey, Object.class);
    }

    @RequestMapping(value = "/management/cache/group/{group}/key/{key}", method = RequestMethod.DELETE)
    public String deleteCacheItem(@PathVariable("group") String group, @PathVariable("key") String key) {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        String encodedKey = convertCacheKey(key);
        Cache cache = cacheGroup(group);
        logger.info("clear cache, group={}, key={}, updatedBy={}", group, encodedKey, requestContext.clientIP());
        cache.evict(encodedKey);
        return String.format("cache item removed, group=%s, key=%s", group, encodedKey);
    }

    private Cache cacheGroup(String group) {
        CacheManager cacheManager = cacheManager();
        Cache cache = cacheManager.getCache(group);
        if (cache == null) throw new ResourceNotFoundException("cache group does not exist, group=" + group);
        return cache;
    }

    private CacheManager cacheManager() {
        try {
            return springObjectFactory.bean(CacheManager.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new InvalidRequestException("cache is not used in this application", e);
        }
    }

    private String convertCacheKey(String key) {
        try {
            DefaultCacheKeyGenerator keyGenerator = springObjectFactory.bean(DefaultCacheKeyGenerator.class);
            return keyGenerator.buildKey(new String[]{key});
        } catch (NoSuchBeanDefinitionException e) {
            throw new InvalidRequestException("cache is not used in this application", e);
        }
    }
}
