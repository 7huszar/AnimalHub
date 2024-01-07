package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.io.IOException;
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
    AnimalDetailDto create(CreateAnimalDto animalDto) throws ValidationException, ConflictException;

    /**
     * Updates the animal with the data given in {@code animalDto} in the persistent data store.
     *
     * @param animalDto the animal to update
     * @return the updated animal
     * @throws ValidationException if the update data given for the animal is in itself incorrect (no name, name too long …)
     * @code NotFoundException when there is no animal with the given ID
     */
    AnimalDetailDto update(AnimalDetailDto animalDto) throws ValidationException, NotFoundException;


    /**
     * Get the animal with given ID from the database.
     *
     * @param id animal id
     * @return the animal with ID {@code id}
     * @code NotFoundException when there is no animal with the given ID
     */
    AnimalDetailDto findById(UUID id) throws NotFoundException;


    /**
     * Delete the animal with the given id from the persistent data store.
     *
     * @param id the id of the animal to be deleted.
     * @code NotFoundException thrown when the id does not point to any animal in the persistent data store.
     */
    void delete(UUID id) throws NotFoundException;


    /**
     * get profile image for animal with given id.
     *
     * @param id id
     * @return FileDetailDto (with actual image file)
     * @throws NotFoundException when the id points to no existing animal
     */
    FileDetailDto getProfileImage(UUID id) throws NotFoundException;

    /**
     * set profile image for animal, works only when image is empty (first setting of image).
     *
     * @param id            id
     * @param createFileDto createFileDto with MultiPartFile as InputFile
     * @return FileDto (without actual image file)
     * @throws ConflictException   when the image is already set
     * @throws ValidationException when the file fails FileValidation
     * @throws IOException         to cover exceptions when creating and saving new file
     * @throws NotFoundException   when the id points to no existing animal
     */
    FileDto setProfileImage(UUID id, CreateFileDto createFileDto) throws ConflictException, ValidationException, IOException, NotFoundException;

    /**
     * update profile image for existing animal which already has an image set.
     *
     * @param id      id
     * @param fileDto fileDto with MultiPartFile as InputFile
     * @return FileDto (without actual image file)
     * @throws ValidationException when the file fails FileValidation
     * @throws IOException         to cover exceptions when creating and saving new file
     * @throws NotFoundException   when the id points to no existing animal
     */
    FileDto updateProfileImage(UUID id, CreateFileDto fileDto) throws ValidationException, IOException, NotFoundException;

    /**
     * Get all the content (images & videos) that has been saved for a specific animal.
     *
     * @param id id
     * @return List of all Content Files in FileDto-format (without the actual image file, for performance reasons)
     * @throws NotFoundException when the id points to no existing animal
     */
    List<FileDto> getContents(UUID id) throws NotFoundException;

    /**
     * Get a specific file from the saved content of an animal.
     *
     * @param id     id
     * @param fileId fileId, also UUID
     * @return Specific File from all the content of a specific animal (with image file)
     * @throws NotFoundException when the id points to no existing animal
     */
    FileDetailDto getContent(UUID id, UUID fileId) throws NotFoundException;

    /**
     * Add a file to the content of an animal.
     *
     * @param id      id
     * @param fileDto fileDto (with MultiPartFile for input file)
     * @return fileDto containing all information about the newly created content file (without image file)
     * @throws ValidationException when the file fails FileValidation
     * @throws IOException         to cover exceptions when creating and saving new file
     * @throws NotFoundException   when the id points to no existing animal
     */
    FileDto addContent(UUID id, CreateFileDto fileDto) throws ValidationException, IOException, NotFoundException;

    /**
     * Delete a specific content file with the given fileId.
     *
     * @param id     id
     * @param fileId fileId
     */
    void deleteContent(UUID id, UUID fileId) throws NotFoundException;
}
