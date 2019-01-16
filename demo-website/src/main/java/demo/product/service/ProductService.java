package demo.product.service;

import com.google.common.base.Strings;
import core.framework.database.JPAAccess;
import core.framework.database.Query;
import demo.product.domain.Product;
import demo.product.web.product.CreateProductRequest;
import demo.product.web.product.ProductQuery;
import demo.product.web.product.UpdateProductRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author chi
 */
public class ProductService {
    @Inject
    JPAAccess repository;

    public Product get(String id) {
        return repository.get(Product.class, id);
    }

    public List<Product> find(ProductQuery productQuery) {
        Query query = Query.create("SELECT t FROM Product t WHERE 1=1");
        if (!Strings.isNullOrEmpty(productQuery.name)) {
            query.append("AND t.name=:name");
            query.param("name", productQuery.name);
        }
        query.fetch(productQuery.limit);
        query.from(productQuery.limit * (productQuery.page - 1));
        return repository.find(query);
    }

    public long count(ProductQuery productQuery) {
        Query query = Query.create("SELECT count(t) FROM Product t WHERE 1=1");
        if (!Strings.isNullOrEmpty(productQuery.name)) {
            query.append("AND t.name=:name");
            query.param("name", productQuery.name);
        }
        return repository.count(query);
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        StringBuilder b = new StringBuilder();
        b.append("DELETE FROM Product t WHERE t.id in (");
        int MAX_GROUP_COUNT = 1000;
        for (int i = 0; i < ids.size(); i++) {
            int index = i % MAX_GROUP_COUNT;
            if (i != 0 && index == 0) {
                b.append(") OR t.id in (");
            } else if (index != 0) {
                b.append(',');
            }
            b.append("'").append(ids.get(i)).append("'");
        }
        b.append(')');
        repository.update(Query.create(b.toString()));
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
    public Product update(String id, UpdateProductRequest request) {
        Product product = get(id);
        product.name = request.name;
        product.description = request.description;
        product.updatedTime = OffsetDateTime.now();
        product.updatedBy = request.requestBy;
        repository.update(product);
        return product;
    }
}
