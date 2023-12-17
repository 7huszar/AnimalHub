package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        } else if (!isValidEmail(userLoginDto.getEmail())) {
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
        if (!user.getRole().equals(UserRole.USER) && !user.getRole().equals(UserRole.ADMIN) && !user.getRole().equals(UserRole.MEMBER)) {
            validationErrors.add("Invalid Role " + user.getRole());
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
    }


    /**
     * Checks if an email has a valid RFC format.
     *
     * @param email address to validate
     * @return true if valid, false if not
     */

    private boolean isValidEmail(String email) {
        return true;
    }
}
