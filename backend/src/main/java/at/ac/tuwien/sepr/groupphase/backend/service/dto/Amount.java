package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Amount(
    @JsonProperty("currency_code") String currencyCode,
    String value
) {
}
