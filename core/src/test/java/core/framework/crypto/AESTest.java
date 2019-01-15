package core.framework.crypto;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * @author neo
 */
public class AESTest {
    @Test
    public void encryptAndDecrypt() throws NoSuchAlgorithmException {
        AES aes = new AES();

        byte[] key = aes.generateKey(128);

        aes.setKey(key);
        String message = "test-message";

        byte[] cipherText = aes.encrypt(message.getBytes());

        byte[] plainBytes = aes.decrypt(cipherText);

        String plainText = new String(plainBytes);

        Assert.assertEquals(message, plainText);
    }
}
