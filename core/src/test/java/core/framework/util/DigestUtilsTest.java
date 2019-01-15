package core.framework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class DigestUtilsTest {
    @Test
    public void md5WithEmptyString() {
        // from http://en.wikipedia.org/wiki/MD5
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", DigestUtils.md5(""));
    }

    @Test
    public void md5() {
        // from http://en.wikipedia.org/wiki/MD5
        assertEquals("e4d909c290d0fb1ca068ffaddf22cbd0", DigestUtils.md5("The quick brown fox jumps over the lazy dog."));
    }
}
