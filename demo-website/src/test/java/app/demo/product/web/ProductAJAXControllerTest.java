package app.demo.product.web;

import com.google.common.collect.Lists;
import app.demo.SpringTest;
import app.demo.common.util.JSONBinder;
import app.demo.product.domain.Product;
import app.demo.product.service.ProductService;
import app.demo.product.web.product.CreateProductRequest;
import app.demo.product.web.product.DeleteProductRequest;
import app.demo.product.web.product.ProductQuery;
import app.demo.product.web.product.UpdateProductRequest;
import app.demo.user.web.user.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductAJAXControllerTest extends SpringTest {
    @Inject
    ProductService productService;

    @Test
    public void getProduct() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.name = "test";
        createProductRequest.description = "test product";
        createProductRequest.requestBy = "SYS";
        Product product = productService.create(createProductRequest);
        mockMvc.perform(get("/admin/api/product/" + product.id)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findProduct() throws Exception {
        ProductQuery productQuery = new ProductQuery();
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/product/find").content(JSONBinder.toJSON(productQuery))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").isNumber());
    }

    @Test
    public void deleteProduct() throws Exception {
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.ids = Lists.newArrayList("1", "2");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/product/batch-delete").content(JSONBinder.toJSON(deleteProductRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk());
    }

    @Test
    public void createProduct() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.name = "test";
        createProductRequest.description = "test product";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/product").content(JSONBinder.toJSON(createProductRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(createProductRequest.name));
    }

    @Test
    public void updateProduct() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.name = "test";
        createProductRequest.description = "test product";
        createProductRequest.requestBy = "SYS";
        Product product = productService.create(createProductRequest);
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.name = "test2";
        updateProductRequest.description = "test product";
        updateProductRequest.requestBy = "SYS";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/product/" + product.id).content(JSONBinder.toJSON(updateProductRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .session((MockHttpSession) getSession()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(updateProductRequest.name));
    }

    @Test
    public void uploadProduct() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", this.getClass().getResourceAsStream("/download/product_template.csv"));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/api/product/upload")
            .file(mockMultipartFile)
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
