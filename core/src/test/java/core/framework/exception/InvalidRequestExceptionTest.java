package core.framework.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class InvalidRequestExceptionTest {
    @Test
    public void createExceptionWithMessageOnly() {
        InvalidRequestException exception = new InvalidRequestException("errorMessage");
        Assert.assertEquals("errorMessage", exception.getMessage());
        Assert.assertNull(exception.field());
    }
}
