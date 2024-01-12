package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = LoginEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class LoginEndpoint {
    static final String BASE_PATH = "/api/v1/authentication";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody UserLoginDto userLoginDto) throws NotAuthorizedException {
        String token;
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("login({})", userLoginDto);
        try {
            token = userService.login(userLoginDto);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            LOGGER.warn(e.getMessage());
            throw new NotAuthorizedException("Failed to login user");
        }
        return token;
    }
}
