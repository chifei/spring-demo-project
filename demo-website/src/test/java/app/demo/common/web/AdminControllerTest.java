package app.demo.common.web;


import app.demo.SpringTest;
import app.demo.common.util.JSONBinder;
import app.demo.user.web.user.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminControllerTest extends SpringTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/admin/")
                .session((MockHttpSession) getSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("/app.html"));
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
