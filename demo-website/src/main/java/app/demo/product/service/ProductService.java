package app.demo.product.service;

import app.demo.common.exception.ResourceNotFoundException;
import app.demo.product.domain.Product;
import app.demo.product.web.product.CreateProductRequest;
import app.demo.product.web.product.ProductQuery;
import app.demo.product.web.product.UpdateProductRequest;
import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Component
public class ProductService {
    @Inject
    ProductRepository repository;

    public Product get(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("missing product, id={}", id));
    }

    public Page<Product> find(ProductQuery productQuery) {
        if (Strings.isNullOrEmpty(productQuery.name)) {
            return repository.findAll(PageRequest.of(productQuery.page - 1, productQuery.limit,
                new Sort(productQuery.desc ? Sort.Direction.DESC : Sort.Direction.ASC, productQuery.sortingField)));
        } else {
            return repository.findByName('%' + productQuery.name + '%', PageRequest.of(productQuery.page - 1, productQuery.limit,
                new Sort(productQuery.desc ? Sort.Direction.DESC : Sort.Direction.ASC, productQuery.sortingField)));
        }
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        repository.batchDelete(ids);
    }

    @Transactional
    public Product create(CreateProductRequest request) {
        Product product = new Product();
        product.id = UUID.randomUUID().toString();
        product.name = request.name;
        product.description = request.description;
        product.createdBy = request.requestBy;
        product.updatedBy = request.requestBy;
        product.createdTime = OffsetDateTime.now();
        product.updatedTime = OffsetDateTime.now();
        repository.save(product);
        return product;
    }

    @Transactional
    public void batchCreate(List<String[]> productList, String requestBy) {
        for (String[] product : productList) {
            CreateProductRequest createProductRequest = new CreateProductRequest();
            createProductRequest.name = product[0];
            createProductRequest.description = product[1];
            createProductRequest.requestBy = requestBy;
            this.create(createProductRequest);
        }
    }

    @Transactional
    public Product update(String id, UpdateProductRequest request) {
        Product product = get(id);
        product.name = request.name;
        product.description = request.description;
        product.updatedTime = OffsetDateTime.now();
        product.updatedBy = request.requestBy;
        repository.save(product);
        return product;
    }
}
