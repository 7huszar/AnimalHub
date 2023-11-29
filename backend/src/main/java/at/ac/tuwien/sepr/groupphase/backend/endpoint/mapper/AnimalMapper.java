package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import org.mapstruct.Mapper;

@Mapper
public interface AnimalMapper {


    Animal createAnimalDtoToAnimal(CreateAnimalDto animalDto);

    AnimalDetailDto animalToAnimalDetailDto(Animal animal);

    Animal animalDetailDtoToAnimal(AnimalDetailDto animalDto);
}

