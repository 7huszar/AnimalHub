package at.ac.tuwien.sepr.groupphase.backend.service.dto;

public enum Intent {
    CAPTURE("CAPTURE"),
    AUTHORIZE("AUTHORIZE");

    private final String value;

    Intent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
