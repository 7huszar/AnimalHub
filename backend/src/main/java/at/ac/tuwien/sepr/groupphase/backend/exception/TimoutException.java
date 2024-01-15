package at.ac.tuwien.sepr.groupphase.backend.exception;

import lombok.Getter;

@Getter
public class TimoutException extends SingleException {
    private final long timeout;
    public TimoutException(String messageSummary, int timeout) {
        super(messageSummary);
        this.timeout = timeout;
    }
}
