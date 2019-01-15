package core.framework.exception;

/**
 * @author neo
 */
public class RemoteServiceException extends RuntimeException {
    private String remoteStackTrace;

    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(String message, String remoteStackTrace) {
        super(message);
        this.remoteStackTrace = remoteStackTrace;
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceException(Throwable cause) {
        super(cause);
    }

    public String remoteStackTrace() {
        return remoteStackTrace;
    }
}
