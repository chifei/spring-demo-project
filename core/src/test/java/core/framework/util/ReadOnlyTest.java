package core.framework.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class ReadOnlyTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void throwExceptionIfAssignTwice() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("oldValue=value1");
        exception.expectMessage("newValue=value2");

        ReadOnly<String> value = new ReadOnly<String>();
        value.set("value1");
        value.set("value2");
    }

    @Test
    public void getValue() {
        ReadOnly<String> value = new ReadOnly<String>();
        value.set("value");

        Assert.assertEquals("value", value.value());
    }

    @Test
    public void convertToString() {
        ReadOnly<String> value = new ReadOnly<String>();
        value.set("value");

        Assert.assertEquals("value", value.toString());
    }

    @Test
    public void equalsWithNull() {
        ReadOnly<String> value = new ReadOnly<String>();

        Assert.assertTrue(value.valueEquals(null));
    }

    @Test
    public void equalsWithValue() {
        ReadOnly<String> value = new ReadOnly<String>();
        value.set("value");

        Assert.assertTrue(value.valueEquals("value"));
    }
}
