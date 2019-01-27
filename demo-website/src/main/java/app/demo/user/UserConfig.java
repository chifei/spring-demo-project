package app.demo.user;

import com.google.common.collect.Lists;
import app.demo.user.domain.PermissionStatus;
import app.demo.user.domain.Role;
import app.demo.user.domain.RoleStatus;
import app.demo.user.service.PermissionService;
import app.demo.user.service.RoleService;
import app.demo.user.service.UserService;
import app.demo.user.web.permission.CreatePermissionRequest;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.user.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Configuration
public class UserConfig {
    private final Logger logger = LoggerFactory.getLogger(UserConfig.class);

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @Inject
    PermissionService permissionService;

    @PostConstruct
    public void init() {
        logger.info("create default users");

        CreateRoleRequest admin = new CreateRoleRequest();
        admin.name = "admin";
        admin.permissions = Lists.newArrayList("user.read", "user.write", "product.read", "product.write");
        admin.requestBy = "SYS";
        admin.status = RoleStatus.ACTIVE;
        Role adminRole = roleService.create(admin);

        CreateRoleRequest operator = new CreateRoleRequest();
        operator.name = "operator";
        operator.permissions = Lists.newArrayList("user.read", "product.read");
        operator.requestBy = "SYS";
        operator.status = RoleStatus.ACTIVE;
        Role operatorRole = roleService.create(operator);

        CreateRoleRequest productMgr = new CreateRoleRequest();
        productMgr.name = "productMgr";
        productMgr.permissions = Lists.newArrayList("product.read", "product.write");
        productMgr.requestBy = "SYS";
        productMgr.status = RoleStatus.ACTIVE;
        Role productRole = roleService.create(productMgr);

        CreateUserRequest createAdminRequest = new CreateUserRequest();
        createAdminRequest.username = "admin";
        createAdminRequest.email = "admin@admin.com";
        createAdminRequest.roleIds = Lists.newArrayList(adminRole.id);
        createAdminRequest.password = "admin";
        createAdminRequest.requestBy = "SYS";
        userService.create(createAdminRequest);

        CreateUserRequest createOperatorUser = new CreateUserRequest();
        createOperatorUser.username = "operator";
        createOperatorUser.email = "operator@admin.com";
        createOperatorUser.roleIds = Lists.newArrayList(operatorRole.id);
        createOperatorUser.password = "operator";
        createOperatorUser.requestBy = "SYS";
        userService.create(createOperatorUser);

        CreateUserRequest createProductManager = new CreateUserRequest();
        createProductManager.username = "productMgr";
        createProductManager.email = "productMgr@admin.com";
        createProductManager.roleIds = Lists.newArrayList(productRole.id);
        createProductManager.password = "productMgr";
        createProductManager.requestBy = "SYS";
        userService.create(createProductManager);

        CreatePermissionRequest createProductReadPermissionRequest = new CreatePermissionRequest();
        createProductReadPermissionRequest.name = "product.read";
        createProductReadPermissionRequest.displayName = "Read Product";
        createProductReadPermissionRequest.description = "User could read product list page.";
        createProductReadPermissionRequest.status = PermissionStatus.ACTIVE;
        createProductReadPermissionRequest.requestBy = "SYS";
        permissionService.create(createProductReadPermissionRequest);

        CreatePermissionRequest createProductWritePermissionRequest = new CreatePermissionRequest();
        createProductWritePermissionRequest.name = "product.write";
        createProductWritePermissionRequest.displayName = "Create or Update Product";
        createProductWritePermissionRequest.description = "User could create or update product.";
        createProductWritePermissionRequest.status = PermissionStatus.ACTIVE;
        createProductWritePermissionRequest.requestBy = "SYS";
        permissionService.create(createProductWritePermissionRequest);

        CreatePermissionRequest createUserReadPermissionRequest = new CreatePermissionRequest();
        createUserReadPermissionRequest.name = "user.read";
        createUserReadPermissionRequest.displayName = "Read User and Role";
        createUserReadPermissionRequest.description = "User could read user list and role list page.";
        createUserReadPermissionRequest.status = PermissionStatus.ACTIVE;
        createUserReadPermissionRequest.requestBy = "SYS";
        permissionService.create(createUserReadPermissionRequest);

        CreatePermissionRequest createUserWritePermissionRequest = new CreatePermissionRequest();
        createUserWritePermissionRequest.name = "user.write";
        createUserWritePermissionRequest.displayName = "Write User and Role";
        createUserWritePermissionRequest.description = "User could write user list and role list page.";
        createUserWritePermissionRequest.status = PermissionStatus.ACTIVE;
        createUserWritePermissionRequest.requestBy = "SYS";
        permissionService.create(createUserWritePermissionRequest);

    }
}
