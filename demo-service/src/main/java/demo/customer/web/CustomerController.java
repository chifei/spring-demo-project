package demo.customer.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import demo.customer.domain.Customer;
import demo.customer.service.CustomerService;
import demo.customer.web.request.RegisterCustomerRequest;
import demo.customer.web.response.CustomerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * @author neo
 */
@RestController
public class CustomerController implements CustomerWebService {
    @Inject
    CustomerService customerService;

    @Override
    public List<CustomerResponse> findAll() {
        List<Customer> customers = customerService.findAll();
        return Lists.transform(customers, new Function<Customer, CustomerResponse>() {
            @Override
            public CustomerResponse apply(Customer customer) {
                return createResponse(customer);
            }
        });
    }

    @Override
    public CustomerResponse create(@Valid @RequestBody RegisterCustomerRequest request) {
        Customer customer = customerService.register(request);
        return createResponse(customer);
    }

    @Override
    public CustomerResponse get(@PathVariable("customerId") Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return createResponse(customer);
    }

    private CustomerResponse createResponse(Customer customer) {
        CustomerResponse view = new CustomerResponse();
        view.id = customer.id;
        view.name = customer.name;
        view.email = customer.email;
        return view;
    }
}
