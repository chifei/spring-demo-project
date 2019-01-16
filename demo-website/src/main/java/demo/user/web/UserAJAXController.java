package demo.user.web;


import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import core.framework.web.i18n.Messages;
import demo.user.domain.Role;
import demo.user.domain.User;
import demo.user.service.RoleService;
import demo.user.service.UserService;
import demo.user.web.role.RoleQuery;
import demo.user.web.user.CreateUserRequest;
import demo.user.web.user.DeleteUserRequest;
import demo.user.web.user.LoginRequest;
import demo.user.web.user.LoginResponse;
import demo.user.web.user.UpdateUserPasswordRequest;
import demo.user.web.user.UpdateUserRequest;
import demo.user.web.user.UserQuery;
import demo.user.web.user.UserQueryResponse;
import demo.user.web.user.UserResponse;
import demo.web.UserInfo;
import demo.web.UserPreference;
import demo.web.interceptor.PermissionRequired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@RestController
public class UserAJAXController {
    @Inject
    Messages messages;
    @Inject
    UserService userService;
    @Inject
    UserInfo userInfo;
    @Inject
    RoleService roleService;
    @Inject
    UserPreference userPreference;

    @RequestMapping(value = "/admin/api/user/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse();
        if (user == null) {
            loginResponse.success = false;
            loginResponse.message = messages.getMessage("user.loginError", userPreference.locale());
        } else {
            loginResponse.success = true;
            userInfo.setUserId(user.id);
            loginResponse.fromURL = "/admin/";
        }
        return loginResponse;
    }

    @RequestMapping(value = "/admin/api/user/{id}", method = RequestMethod.GET)
    @PermissionRequired("user.read")
    public UserResponse get(@PathVariable("id") String id) {
        User user = userService.get(id);
        return response(user);
    }

    @RequestMapping(value = "/admin/api/user/find", method = RequestMethod.PUT)
    @PermissionRequired("user.read")
    public UserQueryResponse find(@RequestBody UserQuery userQuery) {
        UserQueryResponse userQueryResponse = new UserQueryResponse();
        userQueryResponse.items = items(userService.find(userQuery));
        userQueryResponse.page = userQuery.page;
        userQueryResponse.limit = userQuery.limit;
        userQueryResponse.total = userService.count(userQuery);
        return userQueryResponse;
    }

    @RequestMapping(value = "/admin/api/user", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public UserResponse create(@RequestBody CreateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.create(request));
    }

    @RequestMapping(value = "/admin/api/user/{id}", method = RequestMethod.PUT)
    @PermissionRequired("user.write")
    public UserResponse update(@PathVariable("id") String id, @RequestBody UpdateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.update(id, request));
    }

    @RequestMapping(value = "/admin/api/user/{id}/password", method = RequestMethod.PUT)
    @PermissionRequired("user.write")
    public void updatePassword(@PathVariable("id") String id, @RequestBody UpdateUserPasswordRequest request) {
        request.requestBy = userInfo.username();
        userService.updatePassword(id, request);
    }

    @RequestMapping(value = "/admin/api/user/batch-delete", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public void delete(@RequestBody DeleteUserRequest request) {
        userService.batchDelete(request.ids);
    }

    @RequestMapping(value = "/admin/api/user/logout", method = RequestMethod.GET)
    public void logout() {
        userInfo.logout();
    }

    private List<UserResponse> items(List<User> users) {
        Map<String, Role> roles = roleService.find(new RoleQuery()).stream().collect(Collectors.toMap(role -> role.id, role -> role));
        return users.stream().map(user -> {
            UserResponse response = response(user);
            response.roleNames = response.roleIds.stream().map(roleId -> roles.get(roleId).name).collect(Collectors.toList());
            return response;
        }).collect(Collectors.toList());
    }

    private UserResponse response(User user) {
        UserResponse response = new UserResponse();
        response.id = user.id;
        response.username = user.username;
        response.email = user.email;
        if (user.roleIds != null) {
            response.roleIds = Splitter.on(';').splitToList(user.roleIds);
        } else {
            response.roleIds = ImmutableList.of();
        }
        response.roleNames = ImmutableList.of();
        response.createdTime = user.createdTime;
        response.updatedTime = user.updatedTime;
        response.createdBy = user.createdBy;
        response.updatedBy = user.updatedBy;
        return response;
    }
}
