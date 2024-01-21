package at.ac.tuwien.sepr.groupphase.backend.service.conflict;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaypalConflict {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    public void conflictForCaptureOrder(String id) throws ConflictException {
        if (donationRepository.isPayed(id)) {
            throw new ConflictException("conflict capturing order",
                Collections.singletonList("donation already payed"));
        }
    }

    public void conflictForPlacingOrder(UUID id, PaypalDonationDto donationDto) throws ConflictException, NotFoundException {
        ApplicationUser user = userRepository.findById(donationDto.donator()).orElseThrow(NotFoundException::new);

        if (user.getShelter() != null && user.getShelter().getId().equals(id)) {
            throw new ConflictException("conflict placing order",
                Collections.singletonList("donator and receiver are the same"));
        }
    }
}
