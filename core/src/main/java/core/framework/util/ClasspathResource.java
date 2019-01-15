package core.framework.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author neo
 */
public final class ClasspathResource {
    private byte[] bytes;
    private final String resourcePath;

    public ClasspathResource(String resourcePath) {
        this.resourcePath = resourcePath;
        InputStream stream = null;
        try {
            stream = ClasspathResource.class.getClassLoader().getResourceAsStream(resourcePath);
            if (stream == null) throw new IllegalArgumentException("can not load resource, path=" + resourcePath);
            bytes = IOUtils.bytes(stream);
        } finally {
            IOUtils.close(stream);
        }
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public String text() {
        return new String(bytes, Charset.defaultCharset());
    }

    public byte[] bytes() {
        return bytes;
    }

    public String resourcePath() {
        return resourcePath;
    }
}
