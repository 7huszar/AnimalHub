package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordRequestResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator {
    public void validateUserCredentials(UserLoginDto userLoginDto) throws ValidationException {
        List<String> validationErrors = new ArrayList<>();
        if (userLoginDto == null) {
            validationErrors.add("not user data given");
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
        if (userLoginDto.getEmail() == null || userLoginDto.getEmail().isBlank()) {
            validationErrors.add("no e-mail given");
        } else if (isInvalidEmail(userLoginDto.getEmail())) {
            validationErrors.add("invalid e-mail");
        }

        if (userLoginDto.getPassword() == null || userLoginDto.getPassword().isEmpty()) {
            validationErrors.add("no password given");
        } else if (userLoginDto.getPassword().length() < 8) {
            validationErrors.add("password too short " + userLoginDto.getPassword().length() + ", should be 8");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
    }


    public void validateUserCredentialsForUpdate(UserDetailDto user) throws ValidationException {
        List<String> validationErrors = new ArrayList<>();
        if (user == null) {
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
        if (!user.getRole().equals(UserRole.USER) && !user.getRole().equals(UserRole.ADMIN) && !user.getRole().equals(UserRole.MEMBER) && !user.getRole().equals(UserRole.INACTIVE)) {
            validationErrors.add("Invalid Role " + user.getRole());
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
    }


    public void validatePasswordRequestResetDto(PasswordRequestResetDto resetDto) throws ValidationException {
        if (isInvalidEmail(resetDto.email())) {
            throw new ValidationException("Failed validation for password reset request data", Collections.singletonList("invalid e-mail"));
        }
    }

    /**
     * Checks if an email has a valid RFC format.
     *
     * @param email address to validate
     * @return true if valid, false if not
     */

    private boolean isInvalidEmail(String email) {
        return email == null || !Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches();
    }
}
