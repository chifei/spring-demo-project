package core.framework.event;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.google.common.collect.Maps;
import core.framework.log.TraceLogger;
import core.framework.util.JSONBinder;
import core.framework.util.StringUtils;

import javax.inject.Inject;
import java.util.Map;

import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_PUBLISHER;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_SENDER;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_EVENT_TYPE;
import static core.framework.event.SQSEventListenerJob.MESSAGE_ATTR_SOURCE_EVENT_ID;

/**
 * @author neo
 */
public class SNSEventPublisher implements EventPublisher {
    private final AmazonSNS sns;
    private final Map<String, String> topicARNs = Maps.newHashMap();
    private String eventSender;

    @Inject
    TraceLogger traceLogger;

    public SNSEventPublisher(AmazonSNS sns) {
        this.sns = sns;
    }

    public SNSEventPublisher registerEventTopic(Class eventClass, String topicARN) {
        String eventType = EventUtils.type(eventClass);
        topicARNs.put(eventType, topicARN);
        return this;
    }

    @Override
    public void publish(Object event) {
        String type = EventUtils.type(event.getClass());
        String topicARN = topicARNs.get(type);
        if (topicARN == null)
            throw new IllegalStateException("can not find topic, eventType=" + type);

        PublishRequest request = new PublishRequest(topicARN, JSONBinder.toJSON(event), type)
                .addMessageAttributesEntry(MESSAGE_ATTR_EVENT_PUBLISHER, new MessageAttributeValue().withDataType("String").withStringValue("sns"))
                .addMessageAttributesEntry(MESSAGE_ATTR_EVENT_TYPE, new MessageAttributeValue().withDataType("String").withStringValue(type));

        if (StringUtils.hasText(eventSender))
            request.addMessageAttributesEntry(MESSAGE_ATTR_EVENT_SENDER, new MessageAttributeValue().withDataType("String").withStringValue(eventSender));

        if (traceLogger != null)
            request.addMessageAttributesEntry(MESSAGE_ATTR_SOURCE_EVENT_ID, new MessageAttributeValue().withDataType("String").withStringValue(traceLogger.requestId()));

        sns.publish(request);
    }

    public SNSEventPublisher eventSender(String eventSender) {
        this.eventSender = eventSender;
        return this;
    }
}
