package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.MailRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordResetDto;
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
    private static final int MAX_PWD = 255;
    private static final int MIN_PWD = 8;
    private static final int MAX_EMAIL = 254;

    public void validateUserCredentials(UserLoginDto userLoginDto) throws ValidationException {
        List<String> validationErrors = new ArrayList<>();
        if (userLoginDto == null) {
            validationErrors.add("not user data given");
            throw new ValidationException("Failed to validate registration data", validationErrors);
        }
        if (userLoginDto.getEmail() == null || userLoginDto.getEmail().isBlank()) {
            validationErrors.add("no e-mail given");
        } else if (isInvalidEmail(userLoginDto.getEmail())) {
            validationErrors.add("invalid e-mail according to RFC");
        }

        if (!validatePassword(userLoginDto.getPassword()).isEmpty()) {
            validationErrors.add(validatePassword(userLoginDto.getPassword()));
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


    /**
     * Checks if e-mail is valid according to isInvalidEmail(String email).
     *
     * @throws ValidationException if validation failed
     */
    public void validatePasswordRequestResetDto(MailRequestDto resetDto) throws ValidationException {
        if (isInvalidEmail(resetDto.email())) {
            throw new ValidationException("Failed validation for password reset request data", Collections.singletonList("invalid e-mail"));
        }
    }

    /**
     * Validates new password according to this.validatePassword(String password).
     *
     * @throws ValidationException if validation failed
     */
    public void validatePasswordResetDto(PasswordResetDto resetDto) throws ValidationException {
        if (!validatePassword(resetDto.password()).isEmpty()) {
            throw new ValidationException("Failed to validate password for password reset",
                Collections.singletonList(validatePassword(resetDto.password())));
        }
    }

    /**
     * Checks if an email has a valid RFC format. (max length is then 254)
     *
     * @param email address to validate
     * @return true if valid, false if not
     */

    private boolean isInvalidEmail(String email) {
        return email == null || !Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()
            || email.length() > MAX_EMAIL;
    }

    /**
     * Validates password.
     * The criteria are:
     * - null <br>
     * - < MIN_PWD <br>
     * - > MAX_PWD <br>
     *
     * @return String representing failed validation or empty string if validation succeeded
     */
    private String validatePassword(String password) {
        if (password == null) {
            return "no password given";
        }
        if (password.length() < MIN_PWD) {
            return "password has to be at least " + MIN_PWD + " char long; this length: " + password.length();
        }
        if (password.length() > MAX_PWD) {
            return "password max size " + MAX_PWD + "; this length: " + password.length();
        }
        return "";
    }
}