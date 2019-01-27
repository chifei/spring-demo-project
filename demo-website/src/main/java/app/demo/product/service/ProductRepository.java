package app.demo.product.service;

import app.demo.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author chi
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
    @Query("SELECT t FROM Product t where t.name LIKE ?1")
    Page<Product> findByName(String name, Pageable page);

    @Modifying
    @Query("DELETE FROM Product t WHERE t.id IN ?1")
    void batchDelete(List<String> ids);
}
