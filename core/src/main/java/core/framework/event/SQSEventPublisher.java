package core.framework.event;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.common.collect.Maps;
import core.framework.log.TraceLogger;
import core.framework.util.JSONBinder;
import core.framework.util.StopWatch;
import core.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_PUBLISHER;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_SENDER;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_TYPE;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_SOURCE_EVENT_ID;

/**
 * @author neo
 */
public class SQSEventPublisher implements EventPublisher {
    private final Logger logger = LoggerFactory.getLogger(SQSEventPublisher.class);
    private final AmazonSQS sqs;
    private final Map<String, String> queueURLs = Maps.newHashMap();
    private String eventSender;

    @Inject
    TraceLogger traceLogger;

    public SQSEventPublisher(AmazonSQS sqs) {
        this.sqs = sqs;
    }

    public SQSEventPublisher registerEventQueue(Class eventClass, String queueURL) {
        String eventType = EventUtils.type(eventClass);
        queueURLs.put(eventType, queueURL);
        return this;
    }

    @Override
    public void publish(Object event) {
        StopWatch watch = new StopWatch();
        String type = EventUtils.type(event.getClass());
        try {
            String queueURL = queueURLs.get(type);
            if (queueURL == null)
                throw new IllegalStateException("can not find queueURL, eventType=" + type);

            SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(JSONBinder.toJSON(event))
                .addMessageAttributesEntry(MESSAGE_ATTR_EVENT_PUBLISHER, new MessageAttributeValue().withDataType("String").withStringValue("sqs"))
                .addMessageAttributesEntry(MESSAGE_ATTR_EVENT_TYPE, new MessageAttributeValue().withDataType("String").withStringValue(type));

            if (StringUtils.hasText(eventSender))
                request.addMessageAttributesEntry(MESSAGE_ATTR_EVENT_SENDER, new MessageAttributeValue().withDataType("String").withStringValue(eventSender));

            if (traceLogger != null)
                request.addMessageAttributesEntry(MESSAGE_ATTR_SOURCE_EVENT_ID, new MessageAttributeValue().withDataType("String").withStringValue(traceLogger.requestId()));

            sqs.sendMessage(request);
        } finally {
            logger.debug("publish event, type={}, elapsedTime={}", type, watch.elapsedTime());
        }
    }

    public SQSEventPublisher eventSender(String eventSender) {
        this.eventSender = eventSender;
        return this;
    }
}
