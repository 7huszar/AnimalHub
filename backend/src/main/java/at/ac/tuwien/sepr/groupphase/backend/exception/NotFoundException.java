package at.ac.tuwien.sepr.groupphase.backend.exception;


public class NotFoundException extends SingleException {

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String uiErrorSummary, String uiError) {
        super(message, uiErrorSummary, uiError);
    }

}
