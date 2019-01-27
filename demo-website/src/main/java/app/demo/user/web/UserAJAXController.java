package app.demo.user.web;


import app.demo.common.util.Messages;
import app.demo.common.web.UserInfo;
import app.demo.common.web.UserPreference;
import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.user.domain.Role;
import app.demo.user.domain.User;
import app.demo.user.service.RoleService;
import app.demo.user.service.UserRoleService;
import app.demo.user.service.UserService;
import app.demo.user.web.role.RoleQuery;
import app.demo.user.web.user.CreateUserRequest;
import app.demo.user.web.user.DeleteUserRequest;
import app.demo.user.web.user.LoginRequest;
import app.demo.user.web.user.LoginResponse;
import app.demo.user.web.user.UpdateUserPasswordRequest;
import app.demo.user.web.user.UpdateUserRequest;
import app.demo.user.web.user.UserQuery;
import app.demo.user.web.user.UserQueryResponse;
import app.demo.user.web.user.UserResponse;
import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    UserRoleService userRoleService;
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
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.create(request));
    }

    @RequestMapping(value = "/admin/api/user/{id}", method = RequestMethod.PUT)
    @PermissionRequired("user.write")
    public UserResponse update(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.update(id, request));
    }

    @RequestMapping(value = "/admin/api/user/{id}/password", method = RequestMethod.PUT)
    @PermissionRequired("user.write")
    public void updatePassword(@PathVariable("id") String id, @Valid @RequestBody UpdateUserPasswordRequest request) {
        request.requestBy = userInfo.username();
        userService.updatePassword(id, request);
    }

    @RequestMapping(value = "/admin/api/user/batch-delete", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public void delete(@RequestBody DeleteUserRequest request) {
        userService.batchDelete(request);
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
        response.roleIds = userRoleService.getRoleIds(user.id);
        response.roleNames = ImmutableList.of();
        response.status = user.status;
        response.createdTime = user.createdTime;
        response.updatedTime = user.updatedTime;
        response.createdBy = user.createdBy;
        response.updatedBy = user.updatedBy;
        return response;
    }
}
