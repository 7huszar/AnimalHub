package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;

import java.util.List;

public interface AnimalService {

    // TODO doku
    List<Animal> findAll();


    // TODO doku
    AnimalDetailDto create(CreateAnimalDto animalDto);
}
