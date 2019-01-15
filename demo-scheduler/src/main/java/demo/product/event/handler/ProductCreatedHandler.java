package demo.product.event.handler;

import core.framework.database.JPAAccess;
import core.framework.event.EventHandler;
import core.framework.log.ActionLogger;
import demo.product.domain.Product;
import demo.product.event.ProductCreatedEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ProductCreatedHandler implements EventHandler<ProductCreatedEvent> {
    @Inject
    JPAAccess jpaAccess;

    @Inject
    ActionLogger actionLogger;

    @Transactional
    @Override
    public void handle(ProductCreatedEvent event) throws Throwable {
        actionLogger.logContext("product_id", event.productId);
        Product product = new Product();
        product.id = event.productId;
        product.name = event.productName;
        jpaAccess.save(product);
    }
}
