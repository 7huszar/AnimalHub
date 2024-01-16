package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal;

public record PaypalCaptureDto(
    String id,
    String token
) {
}
