package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * Service for working with Files.
 */
public interface FileService {

    /**
     * Get all files from the database.
     *
     * @return all files from the database.
     */
    List<FileDetailDto> findAll();

    /**
     * Validate and then create a new file in the persistent data storage with the given parameters.
     *
     * @param fileDto with file to store
     * @return the created file as FileDto
     * @throws ValidationException if the data given for the animal is in itself incorrect (no name, name too long …)
     */
    FileDto create(CreateFileDto fileDto) throws ValidationException, IOException;

    /**
     * Get the file with given ID from the database.
     *
     * @param id id
     * @return the file with ID {@code id}
     * @code NotFoundException when there is no file with the given ID
     */
    FileDetailDto findById(UUID id) throws NotFoundException;


    /**
     * deletes file with given id.
     *
     * @param id id
     * @throws NotFoundException when the id points to no existing file
     */
    void delete(UUID id) throws NotFoundException;

}
