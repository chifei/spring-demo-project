package core.framework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class ConvertToEnumTest {
    @Test
    public void convertEmptyToEnum() {
        TestEnum value = Convert.toEnum("", TestEnum.class, null);

        Assert.assertNull(value);
    }

    @Test
    public void convertValidNameToEnum() {
        TestEnum value = Convert.toEnum("A", TestEnum.class, null);

        Assert.assertEquals(TestEnum.A, value);
    }

    @Test
    public void convertInValidNameToEnum() {
        TestEnum value = Convert.toEnum("C", TestEnum.class, null);

        Assert.assertNull(value);
    }

    public static enum TestEnum {
        A, B
    }
}
