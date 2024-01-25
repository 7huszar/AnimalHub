package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record DonationSearchDto(
    @Nullable LocalDate dateAfter,
    @Nullable LocalDate dateBefore,
    @Nullable BigDecimal amountAfter,
    @Nullable BigDecimal amountBefore,
    @Nullable String id,
    @Nullable String receiver,
    int page,
    int pageSize
) {
}
