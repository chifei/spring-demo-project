package core.framework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class EncodingUtilsTest {
    @Test
    public void encodeHex() {
        assertEquals("74657374206d657373616765", EncodingUtils.hex("test message".getBytes()));
    }

    @Test
    public void encodeBase64WithEmptyString() {
        assertEquals("", EncodingUtils.base64(""));
    }

    @Test
    public void encodeBase64() {
        // from http://en.wikipedia.org/wiki/Base64
        assertEquals("bGVhc3VyZS4=", EncodingUtils.base64("leasure."));
    }

    @Test
    public void decodeBase64() {
        // from http://en.wikipedia.org/wiki/Base64
        assertEquals("leasure.", new String(EncodingUtils.decodeBase64("bGVhc3VyZS4=")));
    }

    @Test
    public void decodeBase64URLSafe() {
        String message = "leasure.";
        String encodedMessage = EncodingUtils.base64URLSafe(message.getBytes());
        assertEquals(message, new String(EncodingUtils.decodeBase64(encodedMessage)));
    }

    @Test
    public void encodeURL() {
        String urlParamValue = "key=value?";
        // from http://en.wikipedia.org/wiki/Percent-encoding
        assertEquals("key%3Dvalue%3F", EncodingUtils.url(urlParamValue));
    }

    @Test
    public void decodeURL() {
        assertEquals("key=value?", EncodingUtils.decodeURL("key%3Dvalue%3F"));
    }
}
