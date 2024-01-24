package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;

    @Transactional
    @Override
    public List<DonationSumDto> getDonationsByReceiver(UUID shelterId) {
        return donationRepository.getAnimalDonations(shelterId)
            .map(donationSumDto -> new DonationSumDto(donationSumDto.receiver(), donationSumDto.name(),
                donationSumDto.amount() != null ? donationSumDto.amount() : new BigDecimal(0)))
            .collect(Collectors
                .toList());
    }
}
