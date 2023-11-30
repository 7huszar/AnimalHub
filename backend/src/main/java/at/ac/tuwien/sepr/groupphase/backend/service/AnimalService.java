package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;

import java.util.List;
import java.util.UUID;

public interface AnimalService {

    // TODO doku
    List<Animal> findAll();

    // TODO doku
    AnimalDetailDto create(CreateAnimalDto animalDto);

    // TODO doku
    AnimalDetailDto update(AnimalDetailDto animalDto);

    // TODO doku

    AnimalDetailDto getById(UUID id);

}
