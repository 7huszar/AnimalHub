package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShelterValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalShelterMapper mapper;

    // ### HELPER METHODS

    private Boolean stringIsEmpty(String s) {
        if (s != null) {
            return s.isEmpty() || s.isBlank();
        } else {
            return true;
        }
    }

    /**
     * Validates simple attributes of ShelterDtos before Creation or Updating.
     *
     * @param shelterDto shelterDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    void validateShelterDetails(ShelterDto shelterDto) throws ValidationException {
        LOGGER.trace("(validateShelterDetails{})", shelterDto);

        List<String> validationErrors = new ArrayList<>();

        // # Name

        if (stringIsEmpty(shelterDto.getName())) {
            validationErrors.add("No Name given");
        } else if (shelterDto.getName().length() > 200) {
            validationErrors.add("Name is too long");
        }

        // # Address

        if (stringIsEmpty(shelterDto.getAddress())) {
            validationErrors.add("No Address given");
        } else if (shelterDto.getName().length() > 400) {
            validationErrors.add("Address is too long");
        }

        // # Phone Number

        if (stringIsEmpty(shelterDto.getPhoneNumber())) {
            validationErrors.add("No Phone Number given");
        } else if (shelterDto.getName().length() > 50) {
            validationErrors.add("Phone Number is too long");
        }

        // # Email

        if (stringIsEmpty(shelterDto.getName())) {
            validationErrors.add("No Email given");
        } else if (shelterDto.getName().length() > 100) {
            validationErrors.add("Email is too long");
        }

        // # Created On

        // TODO create validation for created on - date


        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of shelter details failed", validationErrors);
        }

    }

    // ### VALIDATION METHODS

    /**
     * Validates new shelter for creation.
     *
     * @param shelterDto shelterDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    public void validateForCreate(CreateShelterDto shelterDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", shelterDto);
        validateShelterDetails(mapper.createShelterDtoToShelterDto(shelterDto));
    }

    /**
     * Validates new shelter for update.
     *
     * @param shelterDto shelterDto
     * @throws ValidationException when at least one value cannot be used / validated
     */
    public void validateForUpdate(ShelterDto shelterDto) throws ValidationException {
        LOGGER.trace("validateForUpdate({})", shelterDto);
        List<String> validationErrors = new ArrayList<>();

        // # id

        if (stringIsEmpty(shelterDto.getId().toString())) {
            validationErrors.add("No id given");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of shelter for update has failed", validationErrors);
        }

        validateShelterDetails(shelterDto);
    }


}
