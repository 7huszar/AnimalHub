package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDto;
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
}
