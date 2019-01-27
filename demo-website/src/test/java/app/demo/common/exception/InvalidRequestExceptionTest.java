package app.demo.common.exception;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidRequestExceptionTest {

    @Test
    public void test() {
        String message = "field invalid";
        InvalidRequestException invalidRequestException = new InvalidRequestException("{}", message);
        Assertions.assertEquals(invalidRequestException.getMessage(), message);
    }

}
