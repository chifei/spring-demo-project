package core.framework.crypto;

import java.io.ByteArrayInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Generate private key:
 * openssl genrsa -out private.pem 1024
 * openssl pkcs8 -topk8 -inform PEM -in private.pem -outform DER -out private.der -nocrypt
 * Generate cert:
 * openssl req -new -x509 -keyform PEM -key private.pem -outform DER -out cert.der
 *
 * @author neo
 */
public class Signature extends RSA {
    private static final String ALGORITHM_SHA1_WITH_RSA = "SHA1withRSA";

    public boolean verify(byte[] message, byte[] signatureValue) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(ALGORITHM_SHA1_WITH_RSA);
            signature.initVerify(publicKey);
            signature.update(message);
            return signature.verify(signatureValue);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] sign(byte[] message) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(ALGORITHM_SHA1_WITH_RSA);
            signature.initSign(privateKey);
            signature.update(message);
            return signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setCertificate(byte[] certificateValue) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificateValue));
            publicKey = certificate.getPublicKey();
        } catch (CertificateException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
