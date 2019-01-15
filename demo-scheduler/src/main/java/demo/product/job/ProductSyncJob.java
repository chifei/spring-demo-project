package demo.product.job;

import core.framework.event.EventPublisher;
import core.framework.scheduler.Job;
import demo.product.event.ProductCreatedEvent;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ProductSyncJob implements Job {
    @Inject
    EventPublisher eventPublisher;

    @Override
    public void execute() throws Throwable {
        for (int i = 0; i < 10; i++) {
            ProductCreatedEvent event = new ProductCreatedEvent();
            event.productId = String.valueOf(i);
            event.productName = "test-" + i;
            eventPublisher.publish(event);
        }
    }
}
