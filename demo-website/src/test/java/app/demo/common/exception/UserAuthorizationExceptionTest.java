package app.demo.common.exception;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserAuthorizationExceptionTest {

    @Test
    public void test() {
        String message = "user not logged in";
        UserAuthorizationException userAuthorizationException = new UserAuthorizationException("{}", message);
        Assertions.assertEquals(userAuthorizationException.getMessage(), message);
    }

}
