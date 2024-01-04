package at.ac.tuwien.sepr.groupphase.backend.enums;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public enum UserRole {

    ADMIN(4, Values.ADMIN_VALUE),
    MEMBER(3, Values.MEMBER_VALUE),
    USER(2, Values.USER_VALUE),
    INACTIVE(1, Values.INACTIVE_ROLE);

    private final int priority;
    private final String value;

    UserRole(int priority, String value) {
        this.priority = priority;
        this.value = value;
    }

    public List<String> getPermissions() {
        var permissions = new LinkedList<String>();

        if (priority >= ADMIN.priority) {
            permissions.add(ADMIN.value);
        }

        if (priority >= MEMBER.priority) {
            permissions.add(MEMBER.value);
        }

        if (priority >= USER.priority) {
            permissions.add(USER.value);
        }

        if (priority >= INACTIVE.priority) {
            permissions.add(INACTIVE.value);
        }

        return permissions;
    }

    public static class Values {
        public static final String ADMIN_VALUE = "ADMIN_ROLE";
        public static final String MEMBER_VALUE = "MEMBER_ROLE";
        public static final String USER_VALUE = "USER_ROLE";
        public static final String INACTIVE_ROLE = "INACTIVE_ROLE";
    }
}
