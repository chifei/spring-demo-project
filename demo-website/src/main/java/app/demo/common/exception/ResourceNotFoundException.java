package app.demo.common.exception;


public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);

    }
}
