package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception thrown is Action of request is not authorized. Http code 401.
 */
public class NotAuthorizedException extends SingleException {
    public NotAuthorizedException() {
        super("");
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, String uiErrorSummary, String uiError) {
        super(message, uiErrorSummary, uiError);
    }

}