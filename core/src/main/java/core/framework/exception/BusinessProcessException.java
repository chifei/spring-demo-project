package core.framework.exception;

/**
 * @author neo
 */
public class BusinessProcessException extends RuntimeException {
    public BusinessProcessException(String message) {
        super(message);
    }

    public BusinessProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}