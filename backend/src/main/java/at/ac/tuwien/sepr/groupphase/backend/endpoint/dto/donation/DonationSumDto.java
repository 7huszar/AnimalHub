package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation;

import java.math.BigDecimal;
import java.util.UUID;

public record DonationSumDto(
    UUID receiver,
    String name,
    BigDecimal amount
) {
}
