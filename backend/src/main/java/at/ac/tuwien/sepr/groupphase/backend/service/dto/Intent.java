package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import lombok.Getter;

@Getter
public enum Intent {
    CAPTURE("CAPTURE"),
    AUTHORIZE("AUTHORIZE");

    private final String value;

    Intent(String value) {
        this.value = value;
    }
}
