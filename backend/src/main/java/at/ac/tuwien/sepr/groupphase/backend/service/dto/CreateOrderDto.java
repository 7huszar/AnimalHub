package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateOrderDto(
    Intent intent,
    @JsonProperty("purchase_units") PurchaseUnits purchaseUnits
) {
}
