package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalExperienceContextDto(
    @JsonProperty("payment_method_preference") String paymentMethodPreference,
    @JsonProperty("user_action") String userAction,
    @JsonProperty("return_url") String returnUrl,
    @JsonProperty("cancel_url") String cancelUrl
) {
}
