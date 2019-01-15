package core.framework.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class EncryptionUtilsTest {
    @Test
    public void encrypt() {
        String[] keyPair = EncryptionUtils.generateKeyPair();

        String encryptedText = EncryptionUtils.encrypt("test", keyPair[0]);

        String decryptedText = EncryptionUtils.decrypt(encryptedText, keyPair[1]);

        Assert.assertEquals("test", decryptedText);
    }
}
