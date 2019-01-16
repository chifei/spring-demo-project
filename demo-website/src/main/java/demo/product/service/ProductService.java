package demo.product.service;

import core.framework.database.JPARepository;
import demo.product.domain.Product;

import javax.inject.Inject;

/**
 * @author chi
 */
public class ProductService {
    @Inject
    JPARepository<Product> repository;
}
