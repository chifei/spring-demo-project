package core.framework.web.request;

import core.framework.exception.InvalidRequestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

/**
 * @author neo
 */
public class RequestIdValidatorTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validateUUIDRequestId() {
        RequestIdValidator.validateRequestId(UUID.randomUUID().toString());
    }

    @Test
    public void validateEmptyRequestId() {
        RequestIdValidator.validateRequestId(null);
        RequestIdValidator.validateRequestId("");
    }

    @Test
    public void validateWithTooLongRequestId() {
        exception.expect(InvalidRequestException.class);
        exception.expectMessage(String.valueOf(RequestIdValidator.REQUEST_ID_MAX_LENGTH));

        RequestIdValidator.validateRequestId("123456789012345678901234567890123456789012345678901");
    }

    @Test
    public void validateWithInvalidChar() {
        exception.expect(InvalidRequestException.class);
        exception.expectMessage(RequestIdValidator.PATTERN_REQUEST_ID.pattern());

        RequestIdValidator.validateRequestId("/../");
    }
}
