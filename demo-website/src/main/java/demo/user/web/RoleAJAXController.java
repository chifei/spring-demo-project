package demo.user.web;


import demo.user.domain.Role;
import demo.user.service.Permission;
import demo.user.service.PermissionService;
import demo.user.service.RoleService;
import demo.user.web.role.CreateRoleRequest;
import demo.user.web.role.RoleQuery;
import demo.user.web.role.RoleQueryResponse;
import demo.user.web.role.UpdateRoleRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
@RestController
public class RoleAJAXController {
    @Inject
    RoleService roleService;

    @Inject
    PermissionService permissionService;

    @RequestMapping(value = "/admin/api/user/role/find", method = RequestMethod.PUT)
    public RoleQueryResponse find(RoleQuery roleQuery) {
        RoleQueryResponse queryResponse = new RoleQueryResponse();
        queryResponse.items = roleService.find(roleQuery);
        queryResponse.total = roleService.count(roleQuery);
        return queryResponse;
    }

    @RequestMapping(value = "/admin/api/user/role/permissions", method = RequestMethod.GET)
    public List<Permission> permissions() {
        return permissionService.permissions();
    }


    @RequestMapping(value = "/admin/api/user/role/{id}", method = RequestMethod.GET)
    public Role get(@PathVariable("id") String id) {
        return roleService.get(id);
    }

    @RequestMapping(value = "/admin/api/user/role/{id}", method = RequestMethod.PUT)
    public Role update(@PathVariable("id") String id, UpdateRoleRequest request) {
        return roleService.update(id, request);
    }

    @RequestMapping(value = "/admin/api/user/role", method = RequestMethod.POST)
    public Role create(CreateRoleRequest request) {
        return roleService.create(request);
    }
}
