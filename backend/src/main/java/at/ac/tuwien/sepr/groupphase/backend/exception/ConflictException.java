package at.ac.tuwien.sepr.groupphase.backend.exception;

import java.util.List;

// TODO make a proper ConflictException class
public class ConflictException extends Exception {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String messageSummary, List<String> errors) {
        super();
    }

}
