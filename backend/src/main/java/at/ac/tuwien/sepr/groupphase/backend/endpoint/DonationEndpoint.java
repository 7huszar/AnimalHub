package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = DonationEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class DonationEndpoint {
    static final String BASE_PATH = "/api/v1/donation";

    private final DonationService donationService;

    @GetMapping("/sum/{shelterId}")
    @Secured(UserRole.Values.MEMBER_VALUE)
    public List<DonationSumDto> donationSums(@PathVariable UUID shelterId) {
        return donationService.getDonationsByReceiver(shelterId);
    }

    @GetMapping("/search/{shelterId}")
    @Secured(UserRole.Values.MEMBER_VALUE)
    public List<DonationListDto> searchDonations(@PathVariable UUID shelterId, DonationSearchDto searchDto) {
        return donationService.findListedDonations(shelterId, searchDto);
    }
}
