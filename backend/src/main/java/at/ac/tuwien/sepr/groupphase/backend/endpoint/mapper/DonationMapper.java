package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface DonationMapper {
    @Mapping(target = "date", source = "stamp", qualifiedByName = "localDateTimeToLocalDate")
    DonationListDto donationToDonationListDto(Donation donation);

    @Named("localDateTimeToLocalDate")
    static LocalDate localDateTimeToLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}
