package core.framework.web.rest.security;

import core.framework.crypto.HMAC;
import core.framework.util.EncodingUtils;

/**
 * @author neo
 */
public class RequestMessage {
    public String uri;
    public String method;
    public String body;

    public String serialize() {
        StringBuilder builder = new StringBuilder(300);
        builder.append("uri=").append(uri).append("&method=")
            .append(method.toUpperCase()).append("&body=");
        if (body != null) builder.append(body);
        return builder.toString();
    }

    public String signByHMAC(HMAC.Hash signatureAlgorithm, String secretKey) {
        HMAC hmac = new HMAC();
        hmac.setHash(signatureAlgorithm);
        hmac.setSecretKey(EncodingUtils.decodeBase64(secretKey));
        return EncodingUtils.base64(hmac.digest(serialize()));
    }
}
