package app.demo.user.web;


import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.user.domain.Permission;
import app.demo.user.service.PermissionService;
import app.demo.user.web.permission.PermissionResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class PermissionAJAXController {

    @Inject
    PermissionService permissionService;

    @RequestMapping(value = "/admin/api/user/permissions", method = RequestMethod.GET)
    @PermissionRequired("user.read")
    public List<PermissionResponse> permissions() {
        return permissionService.permissions().stream().map(permission -> {
            return response(permission);
        }).collect(Collectors.toList());
    }

    private PermissionResponse response(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.id = permission.id;
        response.name = permission.name;
        response.displayName = permission.displayName;
        response.description = permission.description;
        response.status = permission.status;
        response.createdTime = permission.createdTime;
        response.createdBy = permission.createdBy;
        response.updatedTime = permission.updatedTime;
        response.updatedBy = permission.updatedBy;
        return response;
    }

}
