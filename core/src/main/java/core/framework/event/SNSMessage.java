package core.framework.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author neo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SNSMessage {
    @XmlElement(name = "Type")
    public String type;
    @XmlElement(name = "MessageId")
    public String messageId;
    @XmlElement(name = "TopicArn")
    public String topicARN;
    @XmlElement(name = "Subject")
    public String subject;
    @XmlElement(name = "Message")
    public String message;
    @XmlElement(name = "Timestamp")
    public String timestamp;
    @XmlElement(name = "MessageAttributes")
    public SNSMessageAttributes attributes = new SNSMessageAttributes();

    public static class SNSMessageAttributes {
        @XmlElement(name = SQSEventListenerJob.MESSAGE_ATTR_EVENT_SENDER)
        public SNSMessageAttributeValue eventSender;

        @XmlElement(name = SQSEventListenerJob.MESSAGE_ATTR_EVENT_TYPE)
        public SNSMessageAttributeValue eventType;

        @XmlElement(name = SQSEventListenerJob.MESSAGE_ATTR_EVENT_PUBLISHER)
        public SNSMessageAttributeValue eventPublisher;

        @XmlElement(name = SQSEventListenerJob.MESSAGE_ATTR_SOURCE_EVENT_ID)
        public SNSMessageAttributeValue sourceEventId;
    }

    public static class SNSMessageAttributeValue {
        @XmlElement(name = "Type")
        public String type;
        @XmlElement(name = "Value")
        public String value;
    }
}
