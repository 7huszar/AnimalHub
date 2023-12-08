package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception thrown is Action of request is not authorized. Http code 401.
 */
public class NotAuthorizedException extends Exception {
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