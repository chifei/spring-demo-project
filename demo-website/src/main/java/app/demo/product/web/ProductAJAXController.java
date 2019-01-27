package app.demo.product.web;

import app.demo.common.web.UserInfo;
import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.product.domain.Product;
import app.demo.product.service.ProductService;
import app.demo.product.web.product.CreateProductRequest;
import app.demo.product.web.product.DeleteProductRequest;
import app.demo.product.web.product.ProductQuery;
import app.demo.product.web.product.ProductQueryResponse;
import app.demo.product.web.product.ProductResponse;
import app.demo.product.web.product.UpdateProductRequest;
import com.opencsv.CSVReader;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


@RestController
public class ProductAJAXController {

    @Inject
    ProductService productService;
    @Inject
    UserInfo userInfo;

    @RequestMapping(value = "/admin/api/product/{id}", method = RequestMethod.GET)
    @PermissionRequired("product.read")
    public ProductResponse get(@PathVariable("id") String id) {
        Product product = productService.get(id);
        return response(product);
    }

    @RequestMapping(value = "/admin/api/product/find", method = RequestMethod.PUT)
    @PermissionRequired("product.read")
    public ProductQueryResponse find(@RequestBody ProductQuery productQuery) {
        ProductQueryResponse productQueryResponse = new ProductQueryResponse();
        Page<Product> products = productService.find(productQuery);
        productQueryResponse.items = products.map(this::response).stream().collect(Collectors.toList());
        productQueryResponse.page = productQuery.page;
        productQueryResponse.limit = productQuery.limit;
        productQueryResponse.total = products.getTotalElements();
        return productQueryResponse;
    }

    @RequestMapping(value = "/admin/api/product/batch-delete", method = RequestMethod.POST)
    @PermissionRequired("product.write")
    public void delete(@RequestBody DeleteProductRequest request) {
        request.requestBy = userInfo.username();
        productService.batchDelete(request.ids);
    }

    @RequestMapping(value = "/admin/api/product", method = RequestMethod.POST)
    @PermissionRequired("product.write")
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        request.requestBy = userInfo.username();
        return response(productService.create(request));
    }

    @RequestMapping(value = "/admin/api/product/{id}", method = RequestMethod.PUT)
    @PermissionRequired("product.write")
    public ProductResponse update(@PathVariable("id") String id, @Valid @RequestBody UpdateProductRequest request) {
        request.requestBy = userInfo.username();
        return response(productService.update(id, request));
    }

    @RequestMapping("/admin/api/product/upload")
    @PermissionRequired("product.write")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            csvReader.skip(1);
            productService.batchCreate(csvReader.readAll(), userInfo.username());
        }
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
