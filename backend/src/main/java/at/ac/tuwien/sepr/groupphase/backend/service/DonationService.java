package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DonationService {
    List<DonationSumDto> getDonationsByReceiver(UUID shelterId);
}
