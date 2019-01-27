package app.demo.common.web;


import app.demo.SpringTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageAJAXControllerTest extends SpringTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/admin/api/messages/en-US"))
                .andExpect(status().isOk());
    }

}
