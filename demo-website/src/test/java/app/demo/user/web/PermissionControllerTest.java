package app.demo.user.web;

import app.demo.SpringTest;
import app.demo.common.util.JSONBinder;
import app.demo.user.web.user.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PermissionControllerTest extends SpringTest {
    @Test
    public void getPermissions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/user/permissions")
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
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
