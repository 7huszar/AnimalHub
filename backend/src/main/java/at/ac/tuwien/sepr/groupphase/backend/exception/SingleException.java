package at.ac.tuwien.sepr.groupphase.backend.exception;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class SingleException extends Exception {

    private String uiErrorSummary = "";
    private String uiError = "";
    private String code = "";

    public SingleException(String messageSummary) {
        super(messageSummary);
    }

    public SingleException(String messageSummary, String uiErrorSummary, String uiError, String code) {
        super(messageSummary);
        this.uiError = uiError;
        this.uiErrorSummary = uiErrorSummary;
        this.code = code;
    }
}
