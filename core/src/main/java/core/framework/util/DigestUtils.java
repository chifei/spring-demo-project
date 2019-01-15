package core.framework.util;

import com.google.common.base.Charsets;

/**
 * provide wrapper of common codec for convenience
 *
 * @author neo
 */
public final class DigestUtils {
    private DigestUtils() {
    }

    public static String md5(String text) {
        return md5(text.getBytes(Charsets.UTF_8));
    }

    public static String md5(byte[] bytes) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(bytes);
    }

    public static String sha512(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(text);
    }
}
