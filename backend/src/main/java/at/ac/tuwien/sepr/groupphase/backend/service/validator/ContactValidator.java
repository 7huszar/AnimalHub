package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ContactValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
        Pattern.CASE_INSENSITIVE
    );

    private boolean isValidEmailAddress(String email) {
        if (email.length() > 255) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isStringEmpty(String s) {
        return s != null && !s.isBlank();
    }

    public void validateContactMailDto(ContactMailDto contactMailDto) throws ValidationException {
        LOGGER.trace("validateContactMailDto({})", contactMailDto);
        List<String> validationErrors = new ArrayList<>();
        if (isStringEmpty(contactMailDto.from())) {
            validationErrors.add("No email address given");
        } else if (isValidEmailAddress(contactMailDto.from())) {
            validationErrors.add("Invalid email address");
        }
        if (isStringEmpty(contactMailDto.name())) {
            validationErrors.add("No name given");
        }
        if (isStringEmpty(contactMailDto.message())) {
            validationErrors.add("No message given");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate ContactMailDto", validationErrors);
        }
    }

}
