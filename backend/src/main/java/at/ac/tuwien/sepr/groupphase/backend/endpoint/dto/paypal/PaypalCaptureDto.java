package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal;

import java.util.UUID;

public record PaypalCaptureDto(
    String token
) {
}
