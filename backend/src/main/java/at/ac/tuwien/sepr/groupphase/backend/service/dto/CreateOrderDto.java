package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateOrderDto(
    Intent intent,
    @JsonProperty("purchase_units") List<PurchaseUnits> purchaseUnits
) {
}
