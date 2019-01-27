package app.demo.product.web;

import app.demo.SpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductControllerTest extends SpringTest {
    @Test
    public void downloadTemplate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/template/download"))
            .andExpect(status().isOk());
    }

    @Test
    public void exportProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/export"))
            .andExpect(status().isOk());
    }

}
