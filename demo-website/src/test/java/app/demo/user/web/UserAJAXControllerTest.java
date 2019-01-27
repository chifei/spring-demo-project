package app.demo.user.web;

import com.google.common.collect.Lists;
import app.demo.SpringTest;
import app.demo.common.util.JSONBinder;
import app.demo.user.domain.User;
import app.demo.user.service.UserService;
import app.demo.user.web.user.CreateUserRequest;
import app.demo.user.web.user.DeleteUserRequest;
import app.demo.user.web.user.LoginRequest;
import app.demo.user.web.user.UpdateUserPasswordRequest;
import app.demo.user.web.user.UpdateUserRequest;
import app.demo.user.web.user.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserAJAXControllerTest extends SpringTest {
    @Inject
    UserService userService;

    @Test
    public void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "admin";
        loginRequest.password = "admin";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user/login").content(JSONBinder.toJSON(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").isBoolean());
    }

    @Test
    public void getUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = UUID.randomUUID().toString();
        createUserRequest.email = "test@test.com";
        createUserRequest.roleIds = Lists.newArrayList("1");
        createUserRequest.password = "test";
        createUserRequest.requestBy = "SYS";
        User user = userService.create(createUserRequest);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/user/" + user.id)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findUser() throws Exception {
        UserQuery userQuery = new UserQuery();
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/user/find").content(JSONBinder.toJSON(userQuery))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").isNumber());
    }

    @Test
    public void createUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = UUID.randomUUID().toString();
        createUserRequest.email = createUserRequest.username + "@test.com";
        createUserRequest.roleIds = Lists.newArrayList("1");
        createUserRequest.password = "test";
        createUserRequest.requestBy = "SYS";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user").content(JSONBinder.toJSON(createUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(createUserRequest.username));
    }

    @Test
    public void updateUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = "test";
        createUserRequest.email = "test@test.com";
        createUserRequest.roleIds = Lists.newArrayList("1");
        createUserRequest.password = "test";
        createUserRequest.requestBy = "SYS";
        User user = userService.create(createUserRequest);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.email = "test2@test2.com";
        updateUserRequest.requestBy = "SYS";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/user/" + user.id).content(JSONBinder.toJSON(updateUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(updateUserRequest.email));
    }

    @Test
    public void updatePassword() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = UUID.randomUUID().toString();
        createUserRequest.email = createUserRequest.username + "@test.com";
        createUserRequest.roleIds = Lists.newArrayList("1");
        createUserRequest.password = "test";
        createUserRequest.requestBy = "SYS";
        User user = userService.create(createUserRequest);
        UpdateUserPasswordRequest updateUserPasswordRequest = new UpdateUserPasswordRequest();
        updateUserPasswordRequest.password = "test";
        updateUserPasswordRequest.requestBy = "SYS";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/user/" + user.id + "/password").content(JSONBinder.toJSON(updateUserPasswordRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
        deleteUserRequest.ids = Lists.newArrayList("1", "2");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/user/batch-delete").content(JSONBinder.toJSON(deleteUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk());
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/user/logout"))
            .andExpect(status().isOk());
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
