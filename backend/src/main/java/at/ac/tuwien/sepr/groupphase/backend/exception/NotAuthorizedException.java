package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * RuntimeException thrown is Action of request is not authorized. Http code 401.
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedException(Throwable cause) {
        super(cause);
    }
}