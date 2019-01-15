package core.framework.web.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author neo
 */
final class RequestCachingInputStream extends ServletInputStream {
    private final ByteArrayInputStream inputStream;

    public RequestCachingInputStream(byte[] bytes) {
        inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public boolean isFinished() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public boolean isReady() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new IllegalStateException("not implemented");
    }
}
