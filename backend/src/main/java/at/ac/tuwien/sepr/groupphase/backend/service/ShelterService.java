package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;

/**
 * Service to work with Shelters.
 */
public interface ShelterService {

    /**
     * Get all Shelters from database
     *
     * @return List of all Shelters as ShelterDetailDto
     */
    List<ShelterDetailDto> findAll();


    /**
     * Validate and then create a new shelter in the persistent data storage with the given parameters
     *
     * @param shelterDto attributes to create new Shelter
     * @return the created shelter as ShelterDetailDto
     * @throws ValidationException if the data given for the shelter is in itself incorrect (no name, name too long …)
     */
    ShelterDetailDto create(CreateShelterDto shelterDto) throws ValidationException;

    /**
     * Updates the shelter with the data given in {@code shelterDto} in the persistent data store.
     *
     * @param shelterDto the shelter to update
     * @return the updated shelter
     * @throws ValidationException if the update data given for the shelter is in itself incorrect (no name, name too long …)
     * @code NotFoundException when there is no shelter with the given ID
     */
    ShelterDetailDto update(ShelterDetailDto shelterDto)throws ValidationException;

    /**
     * Get the shelter with given ID from the database.
     *
     * @param id shelter id
     * @return the shelter with ID {@code id}
     * @code NotFoundException when there is no shelter with the given ID
     */
    ShelterDetailDto findById(UUID id);

    /**
     * Delete the shelter with the given id from the persistent data store.
     *
     * @param id the id of the shelter to be deleted.
     * @code NotFoundException thrown when the id does not point to any shelter in the persistent data store.
     */
    void delete(UUID id);

}
