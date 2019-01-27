package app.demo.common.exception;


public class ForbiddenException extends ApplicationException {
    public ForbiddenException(String message, Object... args) {
        super(message, args);
    }
}
