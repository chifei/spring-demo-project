package core.framework.event;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.google.common.collect.Maps;
import core.framework.exception.ErrorHandler;
import core.framework.internal.ClassUtils;
import core.framework.log.ActionLogger;
import core.framework.log.TraceLogger;
import core.framework.scheduler.Job;
import core.framework.task.TaskExecutor;
import core.framework.util.AssertUtils;
import core.framework.util.JSONBinder;
import core.framework.util.Threads;
import core.framework.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author neo
 */
public class SQSEventListenerJob implements Job {
    static final String MESSAGE_ATTR_EVENT_PUBLISHER = "event_publisher";
    static final String MESSAGE_ATTR_EVENT_TYPE = "event_type";
    static final String MESSAGE_ATTR_EVENT_SENDER = "event_sender";
    static final String MESSAGE_ATTR_SOURCE_EVENT_ID = "source_event_id";
    private final Logger logger = LoggerFactory.getLogger(SQSEventListenerJob.class);
    private final AmazonSQS sqs;
    private final String queueURL;
    private final Map<String, EventHandler> handlers = Maps.newHashMap();
    private final Map<String, Class> eventClasses = Maps.newHashMap();
    private final AtomicInteger currentHandlers = new AtomicInteger(0);
    @Inject
    TaskExecutor taskExecutor;
    @Inject
    TraceLogger traceLogger;
    @Inject
    ErrorHandler errorHandler;
    @Inject
    ActionLogger actionLogger;
    int maxConcurrentHandlers = 15;

    public SQSEventListenerJob(AmazonSQS sqs, String queueURL) {
        this.sqs = sqs;
        this.queueURL = queueURL;
    }

    <T> void subscribe(Class<T> eventClass, EventHandler<T> eventHandler) {
        String type = EventUtils.type(eventClass);
        eventClasses.put(type, eventClass);
        handlers.put(type, eventHandler);
    }

    @Override
    public void execute() throws Throwable {
        actionLogger.logContext("queue_url", queueURL);
        int pulledEvents = 0;

        for (int i = 0; i < 100; i++) {   // break every 100 batch, about 1000 messages
            List<Message> messages = longPollFromSQS();

            if (messages.isEmpty()) {
                break;
            }

            for (final Message message : messages) {
                taskExecutor.executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        process(message);
                    }
                });
                pulledEvents++;
            }

            while (currentHandlers.get() >= maxConcurrentHandlers) {
                Threads.sleepRoughly(TimeLength.seconds(20));
            }
        }

        actionLogger.logContext("pulled_events", pulledEvents);
    }

    @SuppressWarnings("unchecked")
    private <T> void process(Message message) {
        try {
            currentHandlers.getAndIncrement();
            traceLogger.initialize();
            logger.debug("=== start event handling ===");
            actionLogger.logContext("thread", Thread.currentThread().getId());
            String eventType;
            String messageBody;

            Map<String, MessageAttributeValue> attributes = message.getMessageAttributes();
            MessageAttributeValue publisher = attributes.get(MESSAGE_ATTR_EVENT_PUBLISHER);
            if (publisher != null && "sqs".equals(publisher.getStringValue())) {
                eventType = attributes.get(MESSAGE_ATTR_EVENT_TYPE).getStringValue();
                messageBody = message.getBody();
                traceLogger.requestId(message.getMessageId());

                MessageAttributeValue eventSender = attributes.get(MESSAGE_ATTR_EVENT_SENDER);
                if (eventSender != null) traceLogger.logContext("sender", eventSender.getStringValue());
                MessageAttributeValue sourceEventId = attributes.get(MESSAGE_ATTR_SOURCE_EVENT_ID);
                if (sourceEventId != null) traceLogger.logContext("source_event_id", sourceEventId.getStringValue());
            } else {
                // assume to be SNS
                SNSMessage snsMessage = JSONBinder.fromJSON(SNSMessage.class, message.getBody());
                eventType = snsMessage.subject;
                messageBody = snsMessage.message;
                traceLogger.requestId(snsMessage.messageId);

                if (snsMessage.attributes.eventSender != null) {
                    traceLogger.logContext("sender", snsMessage.attributes.eventSender.value);
                }
                if (snsMessage.attributes.sourceEventId != null) {
                    traceLogger.logContext("source_event_id", snsMessage.attributes.sourceEventId.value);
                }
            }

            AssertUtils.assertHasText(eventType, "eventType should not be empty");

            Class<T> eventClass = eventClasses.get(eventType);
            if (eventClass == null) {
                throw new UnknownEventTypeException("can not find event class, eventType=" + eventType);
            }

            T event = JSONBinder.fromJSON(eventClass, messageBody);
            EventHandler<T> handler = handlers.get(eventType);
            traceLogger.action(ClassUtils.getSimpleOriginalClassName(handler));
            traceLogger.logContext("handler_class", ClassUtils.getOriginalClassName(handler));
            handler.handle(event);
        } catch (Throwable e) {
            logger.debug("message={}", message);
            errorHandler.handle(e);
        } finally {
            currentHandlers.getAndDecrement();
            sqs.deleteMessage(queueURL, message.getReceiptHandle());
            logger.debug("=== finish event handling ===");
            traceLogger.cleanup();
        }
    }

    private List<Message> longPollFromSQS() {
        ReceiveMessageResult result = sqs.receiveMessage(new ReceiveMessageRequest(queueURL)
            .withWaitTimeSeconds(20)
            .withMaxNumberOfMessages(10)
            .withMessageAttributeNames(MESSAGE_ATTR_EVENT_PUBLISHER,
                MESSAGE_ATTR_EVENT_TYPE,
                MESSAGE_ATTR_EVENT_SENDER,
                MESSAGE_ATTR_SOURCE_EVENT_ID));

        List<Message> messages = result.getMessages();
        logger.debug("long poll from SQS, receivedMessages={}", messages.size());
        return messages;
    }
}
