package demo.customer.web;

import demo.customer.web.request.RegisterCustomerRequest;
import demo.customer.web.response.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

/**
 * @author neo
 */
public interface CustomerWebService {
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    List<CustomerResponse> findAll();

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponse create(@Valid @RequestBody RegisterCustomerRequest request);

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    CustomerResponse get(@PathVariable("customerId") Long customerId);
}
