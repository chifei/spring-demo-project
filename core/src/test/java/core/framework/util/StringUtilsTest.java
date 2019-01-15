package core.framework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author neo
 */
public class StringUtilsTest {
    @Test
    public void nullStringsEqual() {
        assertEquals(true, StringUtils.equals(null, null));
    }

    @Test
    public void nullStringNotEqualsToEmpty() {
        assertEquals(false, StringUtils.equals(null, ""));
        assertEquals(false, StringUtils.equals("", null));
    }

    @Test
    public void emptyStringsEqual() {
        assertEquals(true, StringUtils.equals("", ""));
    }

    @Test
    public void nullStringIsLessThanEmptyString() {
        assertEquals(-1, StringUtils.compare(null, ""));
    }

    @Test
    public void nullStringEqualsToNull() {
        assertEquals(0, StringUtils.compare(null, null));
    }

    @Test
    public void compareRegularStrings() {
        assertEquals(1, StringUtils.compare("b", "a"));
    }

    @Test
    public void truncateNull() {
        String value = StringUtils.truncate(null, 10);

        assertNull(value);
    }

    @Test
    public void truncateTextShorterThanMaxLength() {
        String value = StringUtils.truncate("value", 10);

        assertEquals("value", value);
    }

    @Test
    public void truncateTextLongerThanMaxLength() {
        String value = StringUtils.truncate("123456789012345", 10);

        assertEquals("1234567890", value);
    }
}
