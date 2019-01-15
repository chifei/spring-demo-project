package core.test.site.web;

import core.test.site.SpringSiteTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author neo
 */
public class FormTest extends SpringSiteTest {
    @Test
    public void postForm() throws Exception {
        mockMvc.perform(post("/test/form").param("name", "test").contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", equalTo("test")))
            .andExpect(view().name("/form"));
    }
}
