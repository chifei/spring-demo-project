package app.demo.common.exception;


public class UserAuthorizationException extends ApplicationException {
    public UserAuthorizationException(String message, Object... args) {
        super(message, args);
    }
}
