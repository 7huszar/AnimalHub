package at.ac.tuwien.sepr.groupphase.backend.service.dto;

public record AuthResponseDto(
    String scope,
    String accessToken,
    String tokenType,
    String appId,
    long expiresIn,
    String nonce
) {}