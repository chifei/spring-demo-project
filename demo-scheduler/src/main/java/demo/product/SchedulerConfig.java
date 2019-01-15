package demo.product;

import com.amazonaws.services.sqs.AmazonSQS;
import core.framework.scheduler.DefaultSchedulerConfig;
import core.framework.scheduler.JobRegistry;
import core.framework.util.TimeLength;
import demo.product.event.ProductCreatedEvent;
import demo.product.event.handler.ProductCreatedHandler;
import demo.product.job.ProductSyncJob;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author neo
 */
@Configuration
public class SchedulerConfig extends DefaultSchedulerConfig {
    @Inject
    Environment env;
    @Inject
    AmazonSQS amazonSQS;

    @Override
    protected void configure(JobRegistry registry) throws Exception {
        registry.listenSQSEvents(amazonSQS, env.getRequiredProperty("event.queueURL"))
            .subscribe(ProductCreatedEvent.class, new ProductCreatedHandler());

        registry.triggerAtFixedRate(new ProductSyncJob(), TimeLength.seconds(30));
    }
}