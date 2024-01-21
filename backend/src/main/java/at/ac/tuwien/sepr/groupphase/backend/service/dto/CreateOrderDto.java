package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalPaymentSourceDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateOrderDto(
    Intent intent,
    @JsonProperty("purchase_units") List<PurchaseUnits> purchaseUnits,
    @JsonProperty("payment_source")PaypalPaymentSourceDto paymentSource
) {
}
