package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animalDtos.CreateAnimalFileDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalRepository animalRepository;
    // ### HELPER METHODS

    Boolean stringIsEmpty(String s) {
        if (s != null) {
            return s.isEmpty() || s.isBlank();
        } else {
            return true;
        }
    }

    // ### VALIDATION METHODS

    /**
     * Validates new file for creation.
     *
     * @param fileDto fileDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    public void validateForCreate(CreateAnimalFileDto fileDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", fileDto);

        List<String> validationErrors = new ArrayList<>();

        // # Animal Id
        try {
            animalRepository.findById(fileDto.getAnimalId()).orElseThrow(() -> new NotFoundException("Animal with given ID does not exist."));
        } catch (NotFoundException e) {
            validationErrors.add(e.getMessage());
        }

        // # Name

        if (stringIsEmpty(fileDto.getName())) {
            validationErrors.add("No Name given");
        } else if (fileDto.getName().length() > 200) {
            validationErrors.add("Name is too long");
        }

        // # Summary

        if (!stringIsEmpty(fileDto.getSummary())) {
            if (fileDto.getSummary().length() > 400) {
                validationErrors.add("Summary is too long");
            }
        }

        // # File
        if (fileDto.getFile().isEmpty()) {
            validationErrors.add("No File given");
        }
        switch (fileDto.getFile().getContentType()) {
            case "image/bmp":
                // ALLOWED -> No validation error
                break;
            case "image/gif":
                // ALLOWED -> No validation error
                break;
            case "image/jpeg":
                // ALLOWED -> No validation error
                break;
            case "image/png":
                // ALLOWED -> No validation error
                break;
            case "video/mp4":
                // ALLOWED -> No validation error
                break;
            case "video/mpeg":
                // ALLOWED -> No validation error
                break;
            default:
                // ALL OTHER CONTENT TYPES are NOT allowed (for now)!
                validationErrors.add("Content type: '" + fileDto.getFile().getContentType() + "' is not allowed!");
                break;
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation for saving file has failed", validationErrors);
        }

    }


}
