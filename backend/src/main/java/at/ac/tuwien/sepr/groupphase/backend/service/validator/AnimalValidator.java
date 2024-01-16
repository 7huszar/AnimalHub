package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimalValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalShelterMapper mapper;
    private final ShelterService shelterService;

    // ### HELPER METHODS

    /**
     * Checks if String s is null or empty.
     *
     * @param s string to check
     * @return true if null or blank, false otherwise
     */
    private boolean stringIsEmpty(String s) {
        return s == null || s.isBlank();
    }

    /**
     * Checks if date is in past or now.
     *
     * @param date date to check
     * @return true if date is in past or now, false otherwise
     */
    private boolean isValidDateOfBirth(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }

    /**
     * Validates basic attributes of Animal Entities before Creation or Updating. <br>
     *
     * <ul>
     * <li>Name: Must not be empty. Length &lt;= 255.</li>
     * <li>Species: Must not be null.</li>
     * <li>Race: Must not be empty. Length &lt;= 255.</li>
     * <li>Birth Date: Not in future. Age &lt;= 100 years.</li>
     * <li>Height: Must be &gt; 0.</li>
     * <li>Weight: Must be &gt; 0.</li>
     * </ul>
     *
     * @param animalDto animal
     * @return A list of all failed validations
     */
    private List<String> validateAnimalDetails(AnimalDetailDto animalDto) {
        LOGGER.trace("validateAnimalDetails({})", animalDto);

        List<String> validationErrors = new ArrayList<>();

        // # Name

        if (stringIsEmpty(animalDto.getName())) {
            validationErrors.add("No Name given");
        } else if (animalDto.getName().length() > 255) {
            validationErrors.add("Name is too long");
        }

        // # Gender

        if (animalDto.getGender() == null) {
            validationErrors.add("No Gender selected");
        }

        // # English Description

        if (animalDto.getEnglishDescription() != null) {
            if (animalDto.getEnglishDescription().length() > 500) {
                validationErrors.add("English Description is too long");
            }
        }

        // # German Description

        if (animalDto.getGermanDescription() != null) {
            if (animalDto.getGermanDescription().length() > 500) {
                validationErrors.add("German Description is too long");
            }
        }

        // # Species

        if (animalDto.getSpecies() == null) {
            validationErrors.add("No Species selected");
        }

        // # Race

        if (stringIsEmpty(animalDto.getRace())) {
            validationErrors.add("No Race selected");
        } else if (animalDto.getRace().length() > 255) {
            validationErrors.add("Race name is too long");
        }

        // # Birth Date

        if (!isValidDateOfBirth(animalDto.getBirthDate())) {
            validationErrors.add("Birth Date must not be in the future!");
        } else if (animalDto.getBirthDate().isBefore(LocalDate.now().minusYears(100))) {
            validationErrors.add("Animal cannot be older than 50 years!");
        }

        // # Height

        if (animalDto.getHeight() <= 0) {
            validationErrors.add("Height must be greater than zero");
        }

        // # Weight

        if (animalDto.getWeight() <= 0) {
            validationErrors.add("Weight must be greater than zero");
        }

        // # Shelter
        if (animalDto.getShelter() == null || animalDto.getShelter().getId() == null) {
            validationErrors.add("Shelter must not be null");
        }

        return validationErrors;
    }

    // ### VALIDATION METHODS

    /**
     * Validates new animal for creation.
     *
     * @param animalDto animalDto
     * @throws ValidationException if failed to validate animal data
     */
    public void validateForCreate(CreateAnimalDto animalDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", animalDto);
        List<String> validationErrors = validateAnimalDetails(mapper.createAnimalDtoToAnimalDetailDto(animalDto));
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate data for creating animal", validationErrors);
        }
    }

    /**
     * Validates existing animal data for update. <br>
     * Checks if id is null or blank and calls then validateAnimalDetails
     *
     * @param animalDto animalDto
     * @throws ValidationException if failed to validate animal data
     */
    public void validateForUpdate(AnimalDetailDto animalDto) throws ValidationException {
        LOGGER.trace("validateForUpdate({})", animalDto);
        List<String> validationErrors = new ArrayList<>();

        // # id

        if (animalDto.getId() == null || animalDto.getId().toString().isBlank()) {
            validationErrors.add("No id given");
        }

        validationErrors.addAll(validateAnimalDetails(animalDto));

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate data for creating animal", validationErrors);
        }
    }


}
