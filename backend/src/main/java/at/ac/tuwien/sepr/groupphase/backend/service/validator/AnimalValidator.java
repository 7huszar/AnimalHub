package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
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
    private final AnimalMapper mapper;

    // ### HELPER METHODS

    Boolean stringIsEmpty(String s) {
        if (s != null) {
            return s.isEmpty() || s.isBlank();
        } else {
            return true;
        }
    }

    // We expect no animals older than 50 years!
    Boolean isValidDateOfBirth(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }

    /**
     * Validates simple attributes of Animal Entities before Creation or Updating
     *
     * @param animalDto animal
     * @throws ValidationException when at least one value cannot be used / validated
     */
    void validateAnimalDetails(AnimalDetailDto animalDto) throws ValidationException {
        LOGGER.trace("validateAnimalDetails({})", animalDto);

        List<String> validationErrors = new ArrayList<>();

        // # Name

        if (stringIsEmpty(animalDto.getName())) {
            validationErrors.add("No Name given");
        }
        else if(animalDto.getName().length() > 100) {
            validationErrors.add("Name is too long");
        }

        // # Species

        if (stringIsEmpty(animalDto.getSpecies().toString())) {
            validationErrors.add("No Species selected");
        }
        else if(animalDto.getSpecies().toString().length() > 100) {
            validationErrors.add("Species name is too long");
        }

        // # Race

        if (stringIsEmpty(animalDto.getRace().toString())) {
            validationErrors.add("No Race selected");
        }
        else if(animalDto.getRace().toString().length() > 100) {
            validationErrors.add("Race name is too long");
        }

        // # Birth Date

        if (!isValidDateOfBirth(animalDto.getBirthDate())) {
            validationErrors.add("Birth Date must not be in the future!");
        }
        else if(!animalDto.getBirthDate().isBefore(LocalDate.now().minusYears(50))){
            validationErrors.add("Animal cannot be older than 50 years!");
        }

        // # Height

        if (animalDto.getHeight() > 0) {
            validationErrors.add("Height must be greater than zero");
        }

        // # Weight

        if (animalDto.getWeight() > 0) {
            validationErrors.add("Weight must be greater than zero");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of animal details failed", validationErrors);
        }

    }

    // ### VALIDATION METHODS

    /**
     * Validates new animal for creation
     *
     * @param animalDto animalDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    public void validateForCreate(CreateAnimalDto animalDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", animalDto);
        validateAnimalDetails(mapper.createAnimalDtoToAnimalDetailDto(animalDto));
    }

    /**
     * Validates new animal for update
     *
     * @param animalDto animalDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    public void validateForUpdate(AnimalDetailDto animalDto) throws ValidationException {
        LOGGER.trace("validateForUpdate({})", animalDto);
        List<String> validationErrors = new ArrayList<>();

        // # id

        if(stringIsEmpty(animalDto.getId().toString())){
            validationErrors.add("No id given");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of animal for update has failed", validationErrors);
        }

        validateAnimalDetails(animalDto);
    }



}
