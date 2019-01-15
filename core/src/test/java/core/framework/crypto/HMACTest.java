package core.framework.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class HMACTest {
    @Test
    public void authenticateByMD5() {
        HMAC hmac = new HMAC();
        hmac.setSecretKey("4VPDEtyUE".getBytes());
        byte[] bytes = hmac.digest("hello");
        Assert.assertNotNull(bytes);
    }

    @Test
    public void authenticateBySHA1() {
        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA1);
        hmac.setSecretKey("4VPDEtyUE".getBytes());
        byte[] bytes = hmac.digest("hello");
        Assert.assertNotNull(bytes);
    }

    @Test
    public void authenticateBySHA256() {
        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA256);
        hmac.setSecretKey("4VPDEtyUE".getBytes());
        byte[] bytes = hmac.digest("hello");
        Assert.assertNotNull(bytes);
    }

    @Test
    public void authenticateBySHA512() {
        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA512);
        hmac.setSecretKey("123456".getBytes());
        byte[] bytes = hmac.digest("hello");
        Assert.assertNotNull(bytes);
    }

    @Test
    public void generateKey() {
        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA512);
        byte[] key = hmac.generateKey();
        hmac.setSecretKey(key);

        byte[] bytes = hmac.digest("hello");
        Assert.assertNotNull(bytes);
    }
}
