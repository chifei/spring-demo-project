package app.demo.common.exception;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ForbiddenExceptionTest {

    @Test
    public void test() {
        String message = "You don't have permission";
        ForbiddenException forbiddenException = new ForbiddenException("{}", message);
        Assertions.assertEquals(forbiddenException.getMessage(), message);
    }

}
