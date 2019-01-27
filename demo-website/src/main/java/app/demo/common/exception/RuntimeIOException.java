package app.demo.common.exception;


public final class RuntimeIOException extends ApplicationException {
    public RuntimeIOException(String message, Object... args) {
        super(message, args);
    }
}
