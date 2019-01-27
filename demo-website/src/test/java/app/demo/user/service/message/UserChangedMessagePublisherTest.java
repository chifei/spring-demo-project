package app.demo.user.service.message;

import app.demo.SpringTest;
import app.demo.user.domain.User;
import app.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author chi
 */
class UserChangedMessagePublisherTest extends SpringTest {
    @Inject
    UserChangedMessagePublisher userChangedMessagePublisher;

    @Test
    void notifyUserChanged() {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = "test";
        user.email = "test@mail.com";
        user.updatedTime = OffsetDateTime.now();
        user.createdTime = OffsetDateTime.now();
        user.status = UserStatus.ACTIVE;
        user.createdBy = "sys";
        user.updatedBy = "sys";
        userChangedMessagePublisher.notifyUserChanged(user);
    }
}