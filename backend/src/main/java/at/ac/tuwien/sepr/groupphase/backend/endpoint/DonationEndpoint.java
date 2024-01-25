package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationPageDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = DonationEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class DonationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/donation";

    private final DonationService donationService;

    @GetMapping("/sum/{shelterId}")
    @Secured(UserRole.Values.MEMBER_VALUE)
    public List<DonationSumDto> donationSums(@PathVariable UUID shelterId) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("GET " + BASE_PATH + "/sum/" + shelterId);
        LOGGER.debug("donationSums({})", shelterId);

        try {
            return donationService.getDonationsByReceiver(shelterId);
        } catch (NotFoundException | NotAuthorizedException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/search/{shelterId}")
    @Secured(UserRole.Values.MEMBER_VALUE)
    public DonationPageDto searchDonations(@PathVariable UUID shelterId, DonationSearchDto searchDto) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("GET " + BASE_PATH + "/search/" + shelterId);
        LOGGER.debug("searchDonations({}, {})", shelterId, searchDto);

        try {
            return donationService.findListedDonations(shelterId, searchDto);
        } catch (NotFoundException | NotAuthorizedException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        }
    }
}
