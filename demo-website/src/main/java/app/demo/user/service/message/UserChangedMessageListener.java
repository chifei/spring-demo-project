package app.demo.user.service.message;

import app.demo.user.service.Queues;
import app.demo.user.service.UserInfoCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author chi
 */
@Component
public class UserChangedMessageListener {
    private final Logger logger = LoggerFactory.getLogger(UserChangedMessageListener.class);
    @Inject
    UserInfoCacheService userInfoCacheService;

    @JmsListener(destination = Queues.QUEUE_USER)
    public void changed(@Payload UserChangedMessage message) {
        logger.info("user changed, id={}", message);
        userInfoCacheService.evict(message.id);
    }
}
