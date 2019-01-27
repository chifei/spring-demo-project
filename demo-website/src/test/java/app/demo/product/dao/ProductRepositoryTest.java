package app.demo.product.dao;

import app.demo.SpringTest;
import app.demo.product.domain.Product;
import app.demo.product.service.ProductRepository;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
class ProductRepositoryTest extends SpringTest {
    @Inject
    ProductRepository productRepository;

    @Test
    void save() {
        Product product = new Product();
        product.id = UUID.randomUUID().toString();
        product.name = "test";
        product.description = "test";
        product.updatedBy = "SYS";
        product.updatedTime = OffsetDateTime.now();
        product.createdBy = "SYS";
        product.createdTime = OffsetDateTime.now();
        productRepository.save(product);
        assertNotNull(product.id);
    }
}