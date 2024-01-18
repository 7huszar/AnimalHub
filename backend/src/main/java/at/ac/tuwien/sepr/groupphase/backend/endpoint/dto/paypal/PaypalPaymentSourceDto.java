package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalPaymentSourceDto(
    @JsonProperty("paypal") PaypalPaypalDto paypal
) {
}
