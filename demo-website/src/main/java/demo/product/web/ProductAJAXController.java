package demo.product.web;

import demo.product.domain.Product;
import demo.product.service.ProductService;
import demo.product.web.product.CreateProductRequest;
import demo.product.web.product.DeleteProductRequest;
import demo.product.web.product.ProductQuery;
import demo.product.web.product.ProductQueryResponse;
import demo.product.web.product.ProductResponse;
import demo.product.web.product.UpdateProductRequest;
import demo.web.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@RestController
public class ProductAJAXController {

    @Inject
    ProductService productService;
    @Inject
    UserInfo userInfo;

    @RequestMapping(value = "/admin/api/product/{id}", method = RequestMethod.GET)
    public ProductResponse get(@PathVariable("id") String id) {
        Product product = productService.get(id);
        return response(product);
    }

    @RequestMapping(value = "/admin/api/product/find", method = RequestMethod.PUT)
    public ProductQueryResponse find(@RequestBody ProductQuery productQuery) {
        ProductQueryResponse productQueryResponse = new ProductQueryResponse();
        productQueryResponse.items = productService.find(productQuery).stream().map(product -> {
            return response(product);
        }).collect(Collectors.toList());
        productQueryResponse.page = productQuery.page;
        productQueryResponse.limit = productQuery.limit;
        productQueryResponse.total = productService.count(productQuery);
        return productQueryResponse;
    }

    @RequestMapping(value = "/admin/api/product/batch-delete", method = RequestMethod.POST)
    public void delete(@RequestBody DeleteProductRequest request) {
        request.requestBy = userInfo.username();
        productService.batchDelete(request.ids);
    }

    @RequestMapping(value = "/admin/api/product", method = RequestMethod.POST)
    public ProductResponse create(@RequestBody CreateProductRequest request) {
        request.requestBy = userInfo.username();
        return response(productService.create(request));
    }

    @RequestMapping(value = "/admin/api/product/{id}", method = RequestMethod.PUT)
    public ProductResponse update(@PathVariable("id") String id, @RequestBody UpdateProductRequest request) {
        request.requestBy = userInfo.username();
        return response(productService.update(id, request));
    }

    private ProductResponse response(Product product) {
        ProductResponse response = new ProductResponse();
        response.id = product.id;
        response.name = product.name;
        response.description = product.description;
        response.createdTime = product.createdTime;
        response.updatedTime = product.updatedTime;
        response.createdBy = product.createdBy;
        response.updatedBy = product.updatedBy;
        return response;
    }

}
