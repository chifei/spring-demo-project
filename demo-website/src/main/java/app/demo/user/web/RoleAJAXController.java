package app.demo.user.web;


import app.demo.common.web.UserInfo;
import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.user.domain.Role;
import app.demo.user.service.PermissionService;
import app.demo.user.service.RolePermissionService;
import app.demo.user.service.RoleService;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.role.DeleteRoleRequest;
import app.demo.user.web.role.RoleQuery;
import app.demo.user.web.role.RoleQueryResponse;
import app.demo.user.web.role.RoleResponse;
import app.demo.user.web.role.UpdateRoleRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.stream.Collectors;


@RestController
public class RoleAJAXController {
    @Inject
    RoleService roleService;

    @Inject
    PermissionService permissionService;

    @Inject
    RolePermissionService rolePermissionService;

    @Inject
    UserInfo userInfo;

    @RequestMapping(value = "/admin/api/user/role/find", method = RequestMethod.PUT)
    @PermissionRequired("user.read")
    public RoleQueryResponse find(@RequestBody RoleQuery roleQuery) {
        RoleQueryResponse queryResponse = new RoleQueryResponse();
        queryResponse.items = roleService.find(roleQuery).stream().map(this::response).collect(Collectors.toList());
        queryResponse.total = roleService.count(roleQuery);
        return queryResponse;
    }

    @RequestMapping(value = "/admin/api/user/role/{id}", method = RequestMethod.GET)
    @PermissionRequired("user.read")
    public RoleResponse get(@PathVariable("id") String id) {
        return response(roleService.get(id));
    }

    @RequestMapping(value = "/admin/api/user/role/{id}", method = RequestMethod.PUT)
    @PermissionRequired("user.write")
    public RoleResponse update(@PathVariable("id") String id, @Valid @RequestBody UpdateRoleRequest request) {
        request.requestBy = userInfo.username();
        return response(roleService.update(id, request));
    }

    @RequestMapping(value = "/admin/api/user/role", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
        request.requestBy = userInfo.username();
        return response(roleService.create(request));
    }

    @RequestMapping(value = "/admin/api/user/role/batch-delete", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public void delete(@RequestBody DeleteRoleRequest request) {
        roleService.batchDelete(request.ids);
    }

    private RoleResponse response(Role role) {
        RoleResponse response = new RoleResponse();
        response.id = role.id;
        response.name = role.name;
        response.permissions = rolePermissionService.getPermissionNames(role.id);
        response.status = role.status;
        response.createdTime = role.createdTime;
        response.createdBy = role.createdBy;
        response.updatedTime = role.updatedTime;
        response.updatedBy = role.updatedBy;
        return response;
    }

}
