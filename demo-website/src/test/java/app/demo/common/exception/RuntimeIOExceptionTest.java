package app.demo.common.exception;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RuntimeIOExceptionTest {

    @Test
    public void test() {
        String message = "file read fail";
        RuntimeIOException runtimeIOException = new RuntimeIOException("{}", message);
        Assertions.assertEquals(runtimeIOException.getMessage(), message);
    }

}
