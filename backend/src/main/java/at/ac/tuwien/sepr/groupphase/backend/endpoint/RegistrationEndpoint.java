package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordRequestResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.VerificationDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/register")
@RequiredArgsConstructor

public class RegistrationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService service;

    @PostMapping
    public void register(@RequestBody UserLoginDto userLoginDto) throws ValidationException, ConflictException {
        LOGGER.info("POST " + userLoginDto.getEmail());
        service.register(userLoginDto);
    }

    @Secured(UserRole.Values.INACTIVE_ROLE)
    @GetMapping
    @Operation(summary = "request a new mail to verify e-mail", security = @SecurityRequirement(name = "apiKey"))
    public void requestMail(PasswordRequestResetDto mailRequest) {
        LOGGER.info("GET " + mailRequest);
        this.service.requestMail(mailRequest);
    }

    @PutMapping
    public void verify(@RequestBody VerificationDto verificationDto) throws NotAuthorizedException {
        LOGGER.info("PUT " + verificationDto);
        service.verify(verificationDto.getToken());
    }
}
