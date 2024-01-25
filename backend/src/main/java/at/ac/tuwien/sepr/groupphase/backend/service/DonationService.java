package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationPageDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationListDto;

import java.util.List;
import java.util.UUID;

public interface DonationService {

    List<DonationSumDto> getDonationsByReceiver(UUID shelterId);

    DonationPageDto findListedDonations(UUID shelterId, DonationSearchDto searchDto);
}
