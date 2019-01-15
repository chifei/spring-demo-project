package core.framework.crypto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.security.KeyPair;

/**
 * @author neo
 */
public class RSATest {
    RSA rsa;

    @Before
    public void createRSA() {
        rsa = new RSA();
    }

    @Test
    public void encrypt() {
        KeyPair keyPair = rsa.generateKeyPair();
        rsa.setPrivateKey(keyPair.getPrivate().getEncoded());
        rsa.setPublicKey(keyPair.getPublic().getEncoded());
        String message = "test message";
        byte[] encryptedMessage = rsa.encrypt(message.getBytes());
        byte[] decryptedMessage = rsa.decrypt(encryptedMessage);

        Assert.assertEquals(message, new String(decryptedMessage, Charset.forName("UTF-8")));
    }
}
