package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation;

import at.ac.tuwien.sepr.groupphase.backend.enums.DonationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationListDto(
    String id,
    BigDecimal amount,
    LocalDate date,
    DonationType type,
    String receiver
) {
}
