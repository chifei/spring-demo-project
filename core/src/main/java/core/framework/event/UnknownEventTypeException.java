package core.framework.event;

import core.framework.exception.Warning;

/**
 * @author neo
 */
@Warning
public class UnknownEventTypeException extends RuntimeException {
    public UnknownEventTypeException(String message) {
        super(message);
    }
}
