package core.framework.exception;

/**
 * @author neo
 */
@Warning
public class UserAuthorizationException extends RuntimeException {
    public UserAuthorizationException(String message) {
        super(message);
    }

    public UserAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
