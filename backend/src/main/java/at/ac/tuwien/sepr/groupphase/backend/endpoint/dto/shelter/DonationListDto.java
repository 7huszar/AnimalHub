package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationListDto(
    BigDecimal amount,
    LocalDate date
) {
}
