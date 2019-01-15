package core.framework.exception;

/**
 * @author neo
 */
@Warning
public class InvalidRequestException extends RuntimeException {
    private String field;

    public InvalidRequestException(String message) {
        this(null, message, null);
    }

    public InvalidRequestException(String message, Throwable cause) {
        this(null, message, cause);
    }

    public InvalidRequestException(String field, String message) {
        this(field, message, null);
    }

    public InvalidRequestException(String field, String message, Throwable cause) {
        super(message, cause);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
