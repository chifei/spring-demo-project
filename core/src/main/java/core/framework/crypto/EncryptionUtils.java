package core.framework.crypto;

import core.framework.util.CharacterEncodings;
import core.framework.util.ClasspathResource;
import core.framework.util.EncodingUtils;

import java.security.KeyPair;

/**
 * Util to decrypt sensitive data, such as db user password
 * Using RSA+Base64 with PEM encoded keys, and all string based,
 *
 * @author neo
 */
public final class EncryptionUtils {
    public static String encrypt(String plainText, ClasspathResource publicKey) {
        return encrypt(plainText, publicKey.text());
    }

    public static String encrypt(String plainText, String publicKey) {
        RSA rsa = new RSA();
        rsa.setPublicKey(PEM.fromPEM(publicKey));
        byte[] encryptedBytes = rsa.encrypt(plainText.getBytes(CharacterEncodings.CHARSET_UTF_8));
        return EncodingUtils.base64(encryptedBytes);
    }

    public static String decrypt(String encryptedText, ClasspathResource privateKey) {
        return decrypt(encryptedText, privateKey.text());
    }

    public static String decrypt(String encryptedText, String privateKey) {
        RSA rsa = new RSA();
        rsa.setPrivateKey(PEM.fromPEM(privateKey));
        byte[] encryptedBytes = EncodingUtils.decodeBase64(encryptedText);
        byte[] plainText = rsa.decrypt(encryptedBytes);
        return new String(plainText, CharacterEncodings.CHARSET_UTF_8);
    }

    @SuppressWarnings("PMD")    // use system.out to be used as java console app
    public static String[] generateKeyPair() {
        RSA rsa = new RSA();
        KeyPair keyPair = rsa.generateKeyPair();
        String publicKey = PEM.toPEM("RSA PUBLIC KEY", keyPair.getPublic().getEncoded());
        System.out.println(publicKey);
        String privateKey = PEM.toPEM("RSA PRIVATE KEY", keyPair.getPrivate().getEncoded());
        System.out.println(privateKey);
        return new String[]{publicKey, privateKey};
    }

    private EncryptionUtils() {
    }
}
