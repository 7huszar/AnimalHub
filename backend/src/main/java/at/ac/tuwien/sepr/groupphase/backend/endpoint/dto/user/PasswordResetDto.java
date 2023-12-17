package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

public record PasswordResetDto(
    String email,
    String lang
) {
}
