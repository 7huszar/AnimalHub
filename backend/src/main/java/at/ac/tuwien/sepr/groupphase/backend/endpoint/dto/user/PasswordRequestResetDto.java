package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

public record PasswordRequestResetDto(
    String email,
    String lang
) {
}
