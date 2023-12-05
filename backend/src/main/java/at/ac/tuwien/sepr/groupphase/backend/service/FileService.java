package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.io.IOException;


/**
 * Service for working with Files.
 */
public interface FileService {

    /**
     * Validate and then create a new file in the persistent data storage with the given parameters.
     *
     * @param fileDto with file to store
     * @return the created file as FileDetailDto
     * @throws ValidationException if the data given for the animal is in itself incorrect (no name, name too long …)
     */
    FileDetailDto create(CreateFileDto fileDto) throws ValidationException, IOException;

    /**
     * Get the file with given ID from the database.
     *
     * @param id id
     * @return the file with ID {@code id}
     * @code NotFoundException when there is no file with the given ID
     */
    FileDetailDto findById(long id);

}
