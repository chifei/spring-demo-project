package demo.web;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import core.framework.web.site.session.SessionContext;
import core.framework.web.site.session.SessionKey;
import demo.user.domain.Role;
import demo.user.domain.User;
import demo.user.service.RoleService;
import demo.user.service.UserService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
@Component
public class UserInfo {
    public static final String SESSION_USER_ID = "__user_id";

    @Inject
    SessionContext sessionContext;

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    public boolean isUserLogin() {
        String userId = sessionContext.get(SessionKey.stringKey(SESSION_USER_ID));
        return !Strings.isNullOrEmpty(userId);
    }

    public String username() {
        String userId = sessionContext.get(SessionKey.stringKey(SESSION_USER_ID));
        return userService.get(userId).username;
    }

    public void setUserId(String userId) {
        sessionContext.set(SessionKey.stringKey(SESSION_USER_ID), userId);
    }

    public boolean hasPermission(String permission) {
        String userId = sessionContext.get(SessionKey.stringKey(SESSION_USER_ID));
        User user = userService.get(userId);
        if (user.roleIds != null) {
            List<String> roleIds = Splitter.on(';').splitToList(user.roleIds);
            for (String roleId : roleIds) {
                Role role = roleService.get(roleId);
                if (role.permissions.contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> permissions() {
        String userId = sessionContext.get(SessionKey.stringKey(SESSION_USER_ID));
        User user = userService.get(userId);
        List<String> permissions = Lists.newArrayList();
        if (user.roleIds != null) {
            List<String> roleIds = Splitter.on(';').splitToList(user.roleIds);
            for (String roleId : roleIds) {
                Role role = roleService.get(roleId);
                if (role.permissions != null) {
                    permissions.addAll(Splitter.on(';').splitToList(role.permissions));
                }
            }
        }
        return permissions;
    }

    public void logout() {
        sessionContext.invalidate();
    }
}
