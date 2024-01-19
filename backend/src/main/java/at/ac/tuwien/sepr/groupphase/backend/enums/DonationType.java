package at.ac.tuwien.sepr.groupphase.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DonationType {
    ONE_TIME("one time payment"),
    SUBSCRIPTION("subscription");

    private final String description;
}
