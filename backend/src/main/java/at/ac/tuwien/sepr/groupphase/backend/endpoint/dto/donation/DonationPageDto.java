package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation;

import java.util.List;

public record DonationPageDto(
    List<DonationListDto> donations,
    int currentPage,
    int maxPage
) {
}
