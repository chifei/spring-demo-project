package app.demo.user.service.message;

import app.demo.user.domain.User;
import app.demo.user.service.Queues;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author chi
 */
@Component
public class UserChangedMessagePublisher {
    @Inject
    JmsTemplate jmsTemplate;

    public void notifyUserChanged(User user) {
        UserChangedMessage message = new UserChangedMessage();
        message.id = user.id;
        jmsTemplate.convertAndSend(Queues.QUEUE_USER, message);
    }
}
