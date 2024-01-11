package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateSubscriptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = UserEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class UserEndpoint {

    static final String BASE_PATH = "/api/v1/users";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;


    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/current")
    @Operation(summary = "Get the current logged in user", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto getCurrentUser() throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/current");
        LOGGER.debug("getCurrentUser()");

        return userService.getCurrentUser();
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("/search")
    @Operation(summary = "Get list of filtered users", security = @SecurityRequirement(name = "apiKey"))
    public List<UserDetailDto> searchUsers(UserSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH + "/search");
        LOGGER.debug("searchUser({})", searchParameters);

        return userService.search(searchParameters);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws ConflictException, NotAuthorizedException, NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/" + id);
        LOGGER.debug("delete({})", id);

        userService.delete(id);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("{id}")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto findById(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/" + id);
        LOGGER.debug("findById({})", id);

        return userService.findById(id);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @PutMapping("edit/{id}")
    @Operation(summary = "Update specific shelter in database", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto update(@PathVariable UUID id, @Valid @RequestBody UserDetailDto userDetailDto) throws ValidationException, NotFoundException {
        LOGGER.info("PUT " + BASE_PATH + "/edit/" + id);
        LOGGER.debug("update({},{})", id, userDetailDto);

        userDetailDto.setId(id);
        return userService.update(userDetailDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/current")
    @Operation(summary = "Create new subscription in database", security = @SecurityRequirement(name = "apiKey"))
    public SubscriptionDetailDto createSubscription(@Valid @RequestBody CreateSubscriptionDto createSubscriptionDto) {
        LOGGER.info("POST " + BASE_PATH + "/current");
        LOGGER.debug("createSubscription({})", createSubscriptionDto);

        return userService.createSubscription(createSubscriptionDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of subscriptions", security = @SecurityRequirement(name = "apiKey"))
    public List<SubscriptionDetailDto> findByUser(UserDetailDto userDetailDto) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("findByUser({})", userDetailDto);

        return userService.findAllSubscriptionsByUser(userDetailDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @DeleteMapping("/current/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable String id) throws NotFoundException {
        LOGGER.info("DELETE Subscription " + BASE_PATH + "/current/" + id);
        LOGGER.debug("delete({})", id);

        userService.deleteSubscription(id);
    }

}
