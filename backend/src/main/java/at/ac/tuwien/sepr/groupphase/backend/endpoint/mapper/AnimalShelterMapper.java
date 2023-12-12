package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import org.mapstruct.Mapper;

@Mapper
public interface AnimalShelterMapper {

    Animal createAnimalDtoToAnimal(CreateAnimalDto animalDto);

    AnimalDto animalToAnimalDto(Animal animal);

    AnimalDto createAnimalDtoToAnimalDto(CreateAnimalDto animal);

    AnimalDetailDto animalToAnimalDetailDto(Animal animal);

    Animal animalDtoToAnimal(AnimalDto animalDto);

    Animal animalDetailDtoToAnimal(AnimalDetailDto animalDto);

    AnimalDetailDto createAnimalDtoToAnimalDetailDto(CreateAnimalDto animalDto);

    Shelter createShelterDtoToShelter(CreateShelterDto shelterDto);

    ShelterDto shelterToShelterDto(Shelter shelter);

    ShelterDto shelterDetailDtoToShelterDto(ShelterDetailDto shelter);

    ShelterDetailDto shelterToShelterDetailDto(Shelter shelter);

    Shelter shelterDtoToShelter(ShelterDto shelterDto);

    Shelter shelterDetailDtoToShelter(ShelterDetailDto shelterDto);

    ShelterDto createShelterDtoToShelterDto(CreateShelterDto shelterDto);

    ShelterListDto shelterSearchDtoToShelterListDto(ShelterSearchDto shelterSearchDto);

    ShelterListDto shelterToShelterListDto(Shelter shelter);
}
