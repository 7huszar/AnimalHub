package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.MailRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.VerificationDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = RegistrationEndpoint.BASE_PATH)
@RequiredArgsConstructor

public class RegistrationEndpoint {
    static final String BASE_PATH = "/api/v1/register";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserLoginDto userLoginDto) throws ValidationException, ConflictException {
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("register({})", userLoginDto);

        service.register(userLoginDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestMail(MailRequestDto mailRequest) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("requestMail({})", mailRequest);
        try {
            this.service.requestMail(mailRequest);
        } catch (NotFoundException e) {
            LOGGER.warn(e.getMessage());
            // ignore, no info about existence of application user
        }

    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@RequestBody VerificationDto verificationDto) throws NotAuthorizedException {
        LOGGER.info("PUT " + BASE_PATH);
        LOGGER.debug("verify({})", verificationDto);
        try {
            service.verify(verificationDto.getToken());
        } catch (JwtException | NotFoundException e) {
            LOGGER.warn(e.getMessage());
            throw new NotAuthorizedException("could not verify e-mail");
        }

    }
}
