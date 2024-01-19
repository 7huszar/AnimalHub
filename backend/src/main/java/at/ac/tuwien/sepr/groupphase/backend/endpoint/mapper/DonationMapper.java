package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import org.mapstruct.Mapper;

@Mapper
public interface DonationMapper {
    DonationListDto donationToDonationListDto(Donation donation);
}
