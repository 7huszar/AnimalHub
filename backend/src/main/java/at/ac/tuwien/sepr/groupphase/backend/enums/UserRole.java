package at.ac.tuwien.sepr.groupphase.backend.enums;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public enum UserRole {

    ADMIN(3, Values.ADMIN_VALUE),
    MEMBER(2, Values.MEMBER_VALUE),
    USER(1, Values.USER_VALUE);

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

        return permissions;
    }

    public static class Values {
        public static final String ADMIN_VALUE = "ADMIN_ROLE";
        public static final String MEMBER_VALUE = "MEMBER_ROLE";
        public static final String USER_VALUE = "USER_ROLE";
    }
}
