package app.demo.user.web;

import app.demo.SpringTest;
import app.demo.common.util.JSONBinder;
import app.demo.user.domain.Role;
import app.demo.user.domain.RoleStatus;
import app.demo.user.service.RoleService;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.role.DeleteRoleRequest;
import app.demo.user.web.role.RoleQuery;
import app.demo.user.web.role.UpdateRoleRequest;
import app.demo.user.web.user.LoginRequest;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RoleAJAXControllerTest extends SpringTest {
    @Inject
    private RoleService roleService;

    @Test
    public void getRole() throws Exception {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.name = "test";
        createRoleRequest.permissions = Lists.newArrayList("user.read", "product.read");
        createRoleRequest.requestBy = "SYS";
        createRoleRequest.status = RoleStatus.ACTIVE;
        Role role = roleService.create(createRoleRequest);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/user/role/" + role.id)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findRole() throws Exception {
        RoleQuery roleQuery = new RoleQuery();
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/user/role/find").content(JSONBinder.toJSON(roleQuery))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").isNumber());
    }

    @Test
    public void deleteRole() throws Exception {
        DeleteRoleRequest deleteRoleRequest = new DeleteRoleRequest();
        deleteRoleRequest.ids = Lists.newArrayList("1", "2");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user/role/batch-delete").content(JSONBinder.toJSON(deleteRoleRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk());
    }

    @Test
    public void createRole() throws Exception {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.name = "test";
        createRoleRequest.permissions = Lists.newArrayList("user.read", "product.read");
        createRoleRequest.requestBy = "SYS";
        createRoleRequest.status = RoleStatus.ACTIVE;
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user/role").content(JSONBinder.toJSON(createRoleRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(createRoleRequest.name));
    }

    @Test
    public void updateRole() throws Exception {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.name = "test";
        createRoleRequest.permissions = Lists.newArrayList("user.read", "product.read");
        createRoleRequest.requestBy = "SYS";
        createRoleRequest.status = RoleStatus.ACTIVE;
        Role role = roleService.create(createRoleRequest);
        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.name = "test";
        updateRoleRequest.status = RoleStatus.INACTIVE;
        updateRoleRequest.permissions = Lists.newArrayList("user.read", "product.read");
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/user/role/" + role.id).content(JSONBinder.toJSON(updateRoleRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(updateRoleRequest.name));
    }

    private HttpSession getSession() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "admin";
        loginRequest.password = "admin";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user/login").content(JSONBinder.toJSON(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        return result.getRequest().getSession();
    }
}
