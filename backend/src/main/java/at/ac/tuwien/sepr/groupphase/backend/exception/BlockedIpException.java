package at.ac.tuwien.sepr.groupphase.backend.exception;

import lombok.Getter;

@Getter
public class BlockedIpException extends SingleException {

    private final long timeout;

    public BlockedIpException(String messageSummary, long timeout) {
        super(messageSummary);
        this.timeout = timeout;
    }
}
