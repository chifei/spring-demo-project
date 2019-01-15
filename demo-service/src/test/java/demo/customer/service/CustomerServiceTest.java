package demo.customer.service;

import demo.customer.SpringTest;
import demo.customer.domain.Customer;
import demo.customer.web.request.RegisterCustomerRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author neo
 */
public class CustomerServiceTest extends SpringTest {
    @Inject
    CustomerService customerService;

    @Test
    @Transactional
    public void registerCustomer() {
        RegisterCustomerRequest request = new RegisterCustomerRequest();
        request.name = "request";
        request.email = "request@test.com";
        Customer customer = customerService.register(request);

        Assert.assertNotNull(customer.id);
    }
}
