package app.demo;

import app.demo.common.exception.ApplicationException;
import app.demo.user.domain.Role;
import app.demo.user.domain.RoleStatus;
import app.demo.user.service.RoleService;
import app.demo.user.service.UserService;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.user.CreateUserRequest;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author chi
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebConfig.class)
public class SpringTest {
    protected MockMvc mockMvc;

    @Inject
    WebApplicationContext applicationContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        resetDatabase();
    }

    private void resetDatabase() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("TRUNCATE SCHEMA public AND COMMIT")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }

        RoleService roleService = applicationContext.getBean(RoleService.class);
        CreateRoleRequest admin = new CreateRoleRequest();
        admin.name = "admin";
        admin.permissions = Lists.newArrayList("user.read", "user.write", "product.read", "product.write");
        admin.requestBy = "SYS";
        admin.status = RoleStatus.ACTIVE;
        Role adminRole = roleService.create(admin);

        UserService userService = applicationContext.getBean(UserService.class);
        CreateUserRequest createAdminRequest = new CreateUserRequest();
        createAdminRequest.username = "admin";
        createAdminRequest.email = "admin@admin.com";
        createAdminRequest.roleIds = Lists.newArrayList(adminRole.id);
        createAdminRequest.password = "admin";
        createAdminRequest.requestBy = "SYS";
        userService.create(createAdminRequest);
    }
}
