import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClient;
import core.framework.event.EventPublisher;
import core.framework.event.SQSEventPublisher;
import demo.product.event.ProductCreatedEvent;
import demo.product.event.ProductUpdatedEvent;

/**
 * @author neo
 */
public class PublishEventMain {
    public static void main(String[] args) {
        String queueURL = "https://sqs.us-east-1.amazonaws.com/801813351686/neo-test";
        EventPublisher publisher = new SQSEventPublisher(new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider("aws.properties")))
                .registerEventQueue(ProductCreatedEvent.class, queueURL)
                .registerEventQueue(ProductUpdatedEvent.class, queueURL)
                .eventSender("PublishEventMain");

        ProductCreatedEvent event = new ProductCreatedEvent();
        event.productId = "1";
        event.productName = "test";
        publisher.publish(event);

        System.out.println("sent event");
    }
}
