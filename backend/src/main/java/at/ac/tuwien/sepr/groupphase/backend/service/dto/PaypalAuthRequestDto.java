package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalAuthRequestDto(
    @JsonProperty("grant_type") String grantType
) {}
