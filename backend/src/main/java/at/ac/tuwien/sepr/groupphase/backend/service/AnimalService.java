package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface AnimalService {

    // TODO doku
    List<AnimalDetailDto> findAll();

    // TODO doku
    AnimalDetailDto create(CreateAnimalDto animalDto) throws ValidationException;

    // TODO doku
    AnimalDetailDto update(AnimalDetailDto animalDto)throws ValidationException;

    // TODO doku

    AnimalDetailDto findById(UUID id);

}
