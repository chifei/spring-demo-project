package app.demo.common.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author chi
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
        if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
            addSuppressed((Throwable) args[args.length - 1]);
        }
    }
}
