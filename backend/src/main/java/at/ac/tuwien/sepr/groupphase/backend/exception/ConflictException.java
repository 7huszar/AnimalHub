package at.ac.tuwien.sepr.groupphase.backend.exception;

import java.util.List;

// TODO make a proper ConflictException class
public class ConflictException extends ErrorListException {

    public ConflictException(String messageSummary, List<String> errors, List<String> uiErrors, String uiErrorSummary,
                              String uiErrorPrefix) {
        super(messageSummary, errors, uiErrors, uiErrorSummary, uiErrorPrefix);
    }

    public ConflictException(String messageSummary, List<String> errors) {
        super(messageSummary, errors);
    }

}
