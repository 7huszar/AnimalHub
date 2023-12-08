package at.ac.tuwien.sepr.groupphase.backend.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE,
    UNVERIFIED,
    BLOCKED,
    BANNED;

    public static class Values {
        public static final String ACTIVE = "ACTIVE";
        public static final String UNVERIFIED = "UNVERIFIED";
        public static final String BLOCKED = "BLOCKED";
        public static final String BANNED = "BANNED";
    }

}
