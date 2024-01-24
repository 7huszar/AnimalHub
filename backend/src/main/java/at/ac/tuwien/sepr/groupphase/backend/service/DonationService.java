package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;

import java.util.List;
import java.util.UUID;

public interface DonationService {

    List<DonationSumDto> getDonationsByReceiver(UUID shelterId);

    public List<DonationListDto> findListedDonations(UUID shelterId, DonationSearchDto searchDto);
}
