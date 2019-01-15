package demo.customer.service;

import core.framework.database.JPAAccess;
import core.framework.database.Query;
import core.framework.exception.ResourceNotFoundException;
import demo.customer.domain.Customer;
import demo.customer.web.request.RegisterCustomerRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author neo
 */
@Service
public class CustomerService {
    @Inject
    JPAAccess jpaAccess;

    @CacheResult(cacheName = "customers")
    public Customer getCustomerById(Long customerId) {
        Customer customer = jpaAccess.get(Customer.class, customerId);
        if (customer == null) throw new ResourceNotFoundException("customer does not exist, customerId=" + customerId);
        return customer;
    }

    public Customer getCustomerByName(String name) {
        return jpaAccess.findOne(Query.create("from Customer where name= :name").param("name", name));
    }

    public List<Customer> findAll() {
        return jpaAccess.find(Query.create("from Customer"));
    }

    @Transactional
    public Customer register(RegisterCustomerRequest request) {
        Customer customer = new Customer();
        customer.name = request.name;
        customer.email = request.email;
        customer.updatedDate = new Date();
        jpaAccess.save(customer);
        return customer;
    }
}
