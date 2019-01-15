package core.framework.web.site.session;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class SessionKeyTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void typeCannotBeNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(SessionKey.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_NULL);

        new SessionKey<List>("key", null);
    }

    @Test
    public void typeCannotBePrimitive() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(SessionKey.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE);

        new SessionKey<>("key", int.class);
    }

    @Test
    public void typeCannotBeInterface() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(SessionKey.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_INTERFACE);

        new SessionKey<>("key", List.class);
    }
}