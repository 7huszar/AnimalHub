package at.ac.tuwien.sepr.groupphase.backend.service.dto;

import java.util.List;

public record PaypalPlaceOrderResponseDto(
    String id,
    String status,
    List<PaypalLink> links
) {}