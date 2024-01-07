package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class ContactValidator implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean supports(Class<?> clazz) {
        return ContactMailDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContactMailDto contact = (ContactMailDto) target;

        if (StringUtils.isEmpty(contact.name())) {
            errors.rejectValue("name", "Name cannot be empty");
        }

        if (StringUtils.isEmpty(contact.from())) {
            errors.rejectValue("email", "Email cannot be empty");
        } else if (!EMAIL_PATTERN.matcher(contact.from()).matches()) {
            errors.rejectValue("email", "Invalid email format");
        }

    }
}