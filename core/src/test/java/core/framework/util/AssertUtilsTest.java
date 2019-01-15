package core.framework.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class AssertUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void assertNullWithMessageParams() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("message, field=value");

        AssertUtils.assertNotNull(nullValue(), "message, field={}", "value");
    }

    @Test
    public void assertNullWithoutMessageParams() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("message");

        AssertUtils.assertNotNull(nullValue(), "message");
    }

    //Pass findbug
    Object nullValue() {
        return null;
    }

    @Test
    public void assertFalse() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("someErrorMessage");

        AssertUtils.assertFalse(true, "someErrorMessage");
    }

    @Test
    public void assertHasText() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("someErrorMessage");

        AssertUtils.assertHasText(" ", "someErrorMessage");
    }
}
