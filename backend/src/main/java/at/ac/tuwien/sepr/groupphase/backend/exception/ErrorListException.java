package at.ac.tuwien.sepr.groupphase.backend.exception;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * Common super class for exceptions that report a list of errors
 * back to the user, when the given data did not pass a certain kind of checks.
 */
public abstract class ErrorListException extends Exception {
    private final List<String> errors;
    private List<String> uiErrors = null;
    private final String messageSummary;
    @Accessors(fluent = true)
    @Getter
    private String uiErrorSummary = "";
    private String uiErrorPrefix = null;

    public ErrorListException(String messageSummary, List<String> errors, List<String> uiErrors,
                              String uiErrorSummary, String uiErrorPrefix) {
        super(messageSummary);
        this.messageSummary = messageSummary;
        this.errors = errors;
        this.uiErrors = uiErrors;
        this.uiErrorSummary = uiErrorSummary;
        this.uiErrorPrefix = uiErrorPrefix;
    }

    public ErrorListException(String messageSummary, List<String> errors) {
        this.errors = errors;
        this.messageSummary = messageSummary;
    }

    /**
     * See {@link Throwable#getMessage()} for general information about this method.
     *
     * <p>Note: this implementation produces the message
     * from the {@link #summary} and the list of {@link #errors}
     */
    @Override
    public String getMessage() {
        return "%s.: %s."
            .formatted(messageSummary, String.join(", ", errors));
    }

    public String summary() {
        return messageSummary;
    }

    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

    public List<String> uiErrors() {
        return uiErrors != null ? uiErrors.stream().map(e -> uiErrorPrefix + e).toList() : Collections.emptyList();
    }
}
