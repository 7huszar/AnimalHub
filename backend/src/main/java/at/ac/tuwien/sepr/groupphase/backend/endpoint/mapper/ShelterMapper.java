package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import org.mapstruct.Mapper;

@Mapper
public interface ShelterMapper {


    Shelter createShelterDtoToShelter(CreateShelterDto shelterDto);

    ShelterDetailDto shelterToShelterDetailDto(Shelter shelter);

    Shelter shelterDetailDtoToShelter(ShelterDetailDto shelterDto);

    ShelterDetailDto createShelterDtoToShelterDetailDto(CreateShelterDto shelterDto);
}

