package core.test.rest.web;

import core.test.rest.SpringTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author neo
 */
public class ExceptionHandlingTest extends SpringTest {
    @Test
    public void methodNotAllowed() throws Exception {
        mockMvc.perform(get("/test/method-not-allowed"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().contentType(MediaType.APPLICATION_XML))
            .andExpect(xpath("/error/message").string(containsString("not supported")));
    }

    @Test
    public void mediaTypeNotSupported() throws Exception {
        mockMvc.perform(post("/test/media-type-not-supported").content("content").contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(content().contentType(MediaType.APPLICATION_XML))
            .andExpect(xpath("/error/message").string(containsString("not supported")));
    }

    @Test
    public void invalidRequest() throws Exception {
        mockMvc.perform(post("/test/invalid-request").content("<request/>").contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("field_errors[0].field").value(equalTo("field")));
    }

    @Test
    public void notFound() throws Exception {
        // not existing url
        mockMvc.perform(get("/test/not-existing").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value(containsString("not found")));

        // ResourceNotFoundException throws in controller
        mockMvc.perform(get("/test/not-found").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value(containsString("not found")));
    }
}
