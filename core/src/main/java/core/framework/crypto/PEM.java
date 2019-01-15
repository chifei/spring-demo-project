package core.framework.crypto;

import core.framework.util.EncodingUtils;
import core.framework.util.RuntimeIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author neo
 */
public final class PEM {
    public static String toPEM(String type, byte[] content) {
        StringBuilder builder = new StringBuilder("-----BEGIN ")
            .append(type)
            .append("-----");
        String encodedContent = EncodingUtils.base64(content);
        for (int i = 0; i < encodedContent.length(); i++) {
            if (i % 64 == 0) builder.append('\n');
            builder.append(encodedContent.charAt(i));
        }
        builder.append("\n-----END ").append(type).append("-----");
        return builder.toString();
    }

    public static byte[] fromPEM(String pemContent) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(pemContent))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.startsWith("-----")) continue;
                content.append(line.replaceAll("\n", "").replaceAll("\r", ""));
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return EncodingUtils.decodeBase64(content.toString());
    }
}
