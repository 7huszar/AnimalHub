package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;

public record UserSearchDto(
    UserRole role,
    String email
) {
}
