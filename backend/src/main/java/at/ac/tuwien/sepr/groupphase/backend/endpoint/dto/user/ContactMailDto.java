package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

public record ContactMailDto(
    String to,
    String from,
    String name,
    String message
) {}
