package demo.user;

import com.google.common.collect.Lists;
import demo.user.domain.Role;
import demo.user.domain.RoleStatus;
import demo.user.service.RoleService;
import demo.user.service.UserService;
import demo.user.web.role.CreateRoleRequest;
import demo.user.web.user.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author chi
 */
@Configuration
public class UserConfig {
    private final Logger logger = LoggerFactory.getLogger(UserConfig.class);

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @PostConstruct
    public void init() {
        logger.info("create default users");

        CreateRoleRequest admin = new CreateRoleRequest();
        admin.name = "admin";
        admin.permissions = Lists.newArrayList("*");
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
    }
}
