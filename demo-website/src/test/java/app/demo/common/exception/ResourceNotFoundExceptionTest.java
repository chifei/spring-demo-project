package app.demo.common.exception;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionTest {

    @Test
    public void test() {
        String message = "404";
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("{}", message);
        Assertions.assertEquals(resourceNotFoundException.getMessage(), message);
    }

}
