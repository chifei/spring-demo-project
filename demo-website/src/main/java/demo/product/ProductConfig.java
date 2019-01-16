package demo.product;

import demo.product.service.ProductService;
import demo.product.web.product.CreateProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author chi
 */
@Configuration
public class ProductConfig {
    private final Logger logger = LoggerFactory.getLogger(ProductConfig.class);

    @Inject
    ProductService productService;

    @PostConstruct
    public void init() {
        logger.info("create default products");

        CreateProductRequest createIphoneProductRequest = new CreateProductRequest();
        createIphoneProductRequest.name = "iPhone";
        createIphoneProductRequest.description = "Apple iPhone XR";
        createIphoneProductRequest.requestBy = "SYS";
        productService.create(createIphoneProductRequest);

        CreateProductRequest createHuaWeiProductRequest = new CreateProductRequest();
        createHuaWeiProductRequest.name = "Mate20";
        createHuaWeiProductRequest.description = "HUAWEI Mate 20";
        createHuaWeiProductRequest.requestBy = "SYS";
        productService.create(createHuaWeiProductRequest);
    }
}
