package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Service to work with Shelters.
 */
public interface ShelterService {

    /**
     * Get all Shelters from database.
     *
     * @return List of all Shelters as ShelterDetailDto
     */
    List<ShelterDto> findAll();


    /**
     * Validate and then create a new shelter in the persistent data storage with the given parameters.
     *
     * @param shelterDto attributes to create new Shelter
     * @return the created shelter as ShelterDetailDto
     * @throws ValidationException if the data given for the shelter is in itself incorrect (no name, name too long …)
     */
    ShelterDto create(CreateShelterDto shelterDto) throws ValidationException;

    /**
     * Updates the shelter with the data given in {@code shelterDto} in the persistent data store.
     *
     * @param shelterDto the shelter to update
     * @return the updated shelter
     * @throws ValidationException if the update data given for the shelter is in itself incorrect (no name, name too long …)
     * @code NotFoundException when there is no shelter with the given ID
     */
    ShelterDto update(ShelterDto shelterDto) throws ValidationException, NotFoundException;


    /**
     * Search for shelters in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in user.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the shelters where the given fields match.
     */
    List<ShelterListDto> search(ShelterSearchDto searchParameters);

    /**
     * Get the shelter with given ID from the database.
     *
     * @param id shelter id
     * @return the shelter with ID {@code id}
     * @code NotFoundException when there is no shelter with the given ID
     */
    ShelterDetailDto findById(UUID id) throws NotFoundException;

    /**
     * Delete the shelter with the given id from the persistent data store.
     *
     * @param id the id of the shelter to be deleted.
     * @code NotFoundException thrown when the id does not point to any shelter in the persistent data store.
     */
    void delete(UUID id) throws NotFoundException;

    /**
     * Get the profile image of the shelter with given id.
     *
     * @param id id
     * @return FileDetailDto of profile image (contains actual image file)
     * @throws NotFoundException when the id points to no existing shelter
     */
    FileDetailDto getProfileImage(UUID id) throws NotFoundException;

    /**
     * Set the profile image when first creating the shelter.
     *
     * @param id            id
     * @param createFileDto createFileDto
     * @return FileDto of profile image (does NOT contain actual image file)
     * @throws ConflictException   when there is already a profile image set (then you should use update)
     * @throws ValidationException when there is a validation error with the given createFileDto (empty image file, etc.)
     * @throws IOException         when there is a problem with processing the file itself
     * @throws NotFoundException   when the id points to no existing shelter
     */
    FileDto setProfileImage(UUID id, CreateFileDto createFileDto) throws ConflictException, ValidationException, IOException, NotFoundException;

    /**
     * Similar to setProfileImage but only used after creation when user wants to change the existing profile image.
     *
     * @param id            id
     * @param createFileDto createFileDto
     * @return FileDto of profile image (does NOT contain actual image file)
     * @throws ValidationException when there is a validation error with the given createFileDto (empty image file, etc.)
     * @throws IOException         when there is a problem with processing the file itself
     * @throws NotFoundException   when the id points to no existing shelter
     */
    FileDto updateProfileImage(UUID id, CreateFileDto createFileDto) throws ValidationException, IOException, NotFoundException;
}
