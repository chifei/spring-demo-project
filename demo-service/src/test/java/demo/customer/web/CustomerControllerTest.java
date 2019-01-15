package demo.customer.web;

import core.framework.util.JSONBinder;
import demo.customer.SpringTest;
import demo.customer.domain.Customer;
import demo.customer.service.CustomerService;
import demo.customer.web.request.RegisterCustomerRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author neo
 */
public class CustomerControllerTest extends SpringTest {
    @Inject
    CustomerService customerService;

    @Test
    @Transactional
    public void get() throws Exception {
        RegisterCustomerRequest request = createRegisterCustomerRequest();
        Customer customer = customerService.register(request);

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/" + customer.id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/1000")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void register() throws Exception {
        RegisterCustomerRequest request = createRegisterCustomerRequest();

        String requestBody = JSONBinder.toJSON(request);
        mockMvc.perform(post("/customers").content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    private RegisterCustomerRequest createRegisterCustomerRequest() {
        RegisterCustomerRequest request = new RegisterCustomerRequest();
        request.name = "test";
        request.email = "test@test.com";
        return request;
    }
}
