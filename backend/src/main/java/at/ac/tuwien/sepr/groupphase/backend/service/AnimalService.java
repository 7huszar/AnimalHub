package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;


/**
 * Service for working with Animals.
 */
public interface AnimalService {

    /**
     * Get all Animals from database.
     *
     * @return List of all Animals as AnimalDetailDto
     */
    List<AnimalDetailDto> findAll();

    /**
     * Validate and then create a new animal in the persistent data storage with the given parameters.
     *
     * @param animalDto attributes to create new Animal
     * @return the created animal as AnimalDetailDto
     * @throws ValidationException if the data given for the animal is in itself incorrect (no name, name too long …)
     */
    AnimalDetailDto create(CreateAnimalDto animalDto) throws ValidationException;

    /**
     * Updates the animal with the data given in {@code animalDto} in the persistent data store.
     *
     * @param animalDto the animal to update
     * @return the updated animal
     * @throws ValidationException if the update data given for the animal is in itself incorrect (no name, name too long …)
     * @code NotFoundException when there is no animal with the given ID
     */
    AnimalDetailDto update(AnimalDetailDto animalDto) throws ValidationException;


    /**
     * Get the animal with given ID from the database.
     *
     * @param id animal id
     * @return the animal with ID {@code id}
     * @code NotFoundException when there is no animal with the given ID
     */
    AnimalDetailDto findById(UUID id);


    /**
     * Delete the animal with the given id from the persistent data store.
     *
     * @param id the id of the animal to be deleted.
     * @code NotFoundException thrown when the id does not point to any animal in the persistent data store.
     */
    void delete(UUID id);

}
