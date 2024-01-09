package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordRequestResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordResetDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = PasswordEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class PasswordEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService service;
    static final String BASE_PATH = "/api/v1/password";

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestPasswordRequest(PasswordRequestResetDto passwordRequestResetDto) throws ValidationException {
        LOGGER.info("GET " + BASE_PATH + "{}", passwordRequestResetDto.email());
        try {
            service.requestPasswordReset(passwordRequestResetDto);
        } catch (NotFoundException | NotAuthorizedException e) {
            LOGGER.warn(e.getMessage());
            // Do nothing, so malicious user can't check for existence of e-mail in system
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody PasswordResetDto resetDto) throws NotAuthorizedException {
        LOGGER.info("PUT " + BASE_PATH + "{}", resetDto.token());
        try {
            service.resetPassword(resetDto);
        } catch (NotFoundException | JwtException e) {
            LOGGER.warn(e.getMessage());
            throw new NotAuthorizedException("Could not reset password");
        }

    }
}
