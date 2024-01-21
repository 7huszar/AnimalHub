package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponseDto(
    String scope,
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("app_id") String appId,
    @JsonProperty("expires_in") long expiresIn,
    String nonce
) {}