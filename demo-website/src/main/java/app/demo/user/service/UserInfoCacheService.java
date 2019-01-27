package app.demo.user.service;

import app.demo.user.domain.Role;
import app.demo.user.domain.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
@Component
public class UserInfoCacheService {
    private final Logger logger = LoggerFactory.getLogger(UserInfoCacheService.class);

    @Inject
    UserService userService;

    @Inject
    UserRoleService userRoleService;

    @Inject
    RoleService roleService;

    @Inject
    RolePermissionService rolePermissionService;

    @Inject
    CacheManager cacheManager;

    @Cacheable(Caches.CACHE_USER_INFO)
    public CachedUserInfo load(String id) {
        logger.info("load cache, cache={}, userId={}", Caches.CACHE_USER_INFO, id);
        User user = userService.get(id);
        List<String> roleIds = userRoleService.getRoleIds(user.id);
        List<String> permissionNames = Lists.newArrayList();
        if (roleIds != null && !roleIds.isEmpty()) {
            for (String roleId : roleIds) {
                Role role = roleService.get(roleId);
                permissionNames.addAll(rolePermissionService.getPermissionNames(role.id));
            }
        }
        CachedUserInfo cachedUserInfo = new CachedUserInfo();
        cachedUserInfo.id = user.id;
        cachedUserInfo.username = user.username;
        cachedUserInfo.roleIds = roleIds;
        cachedUserInfo.permissions = permissionNames;
        return cachedUserInfo;
    }

    public void evict(String id) {
        Cache cache = cacheManager.getCache(Caches.CACHE_USER_INFO);
        if (cache != null) {
            cache.evict(id);
        }
    }

    public void evictAll() {
        Cache cache = cacheManager.getCache(Caches.CACHE_USER_INFO);
        if (cache != null) {
            cache.clear();
        }
    }
}
