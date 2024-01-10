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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    private RestTemplate restTemplate;


    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/current")
    @Operation(summary = "Get the current logged in user", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto getCurrentUser() throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/current");

        return userService.getCurrentUser();
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("/search")
    @Operation(summary = "Get list of filtered users", security = @SecurityRequirement(name = "apiKey"))
    public List<UserDetailDto> searchUsers(UserSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("request parameters: {}", searchParameters);

        return userService.search(searchParameters);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws ConflictException, NotAuthorizedException, NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        userService.delete(id);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("{id}")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto findById(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/{}", id);
        return userService.findById(id);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @PutMapping("edit/{id}")
    @Operation(summary = "Update specific shelter in database", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto update(@PathVariable UUID id, @Valid @RequestBody UserDetailDto userDetailDto) throws ValidationException, NotFoundException {
        LOGGER.info("PUT " + BASE_PATH + "/{}", id);
        LOGGER.debug("RequestBody:{}", userDetailDto);
        userDetailDto.setId(id);

        return userService.update(userDetailDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/current")
    @Operation(summary = "Create new subscription in database", security = @SecurityRequirement(name = "apiKey"))
    public SubscriptionDetailDto createSubscription(@Valid @RequestBody CreateSubscriptionDto createSubscriptionDto) {
        LOGGER.info("POST " + BASE_PATH + "/");
        LOGGER.debug("Body of request:\n{}", createSubscriptionDto);
        return userService.createSubscription(createSubscriptionDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of subscriptions", security = @SecurityRequirement(name = "apiKey"))
    public List<SubscriptionDetailDto> findByUser(UserDetailDto userDetailDto) {
        LOGGER.info("GET " + BASE_PATH);

        return userService.findAllSubscriptionsByUser(userDetailDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @DeleteMapping("/current/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable String id) throws NotFoundException {
        LOGGER.info("DELETE Subscription " + BASE_PATH);
        userService.deleteSubscription(id);
    }

    //TODO make this delete instead so there is no return value
    @Secured(UserRole.Values.USER_VALUE)
    @DeleteMapping("/paypal/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelSubscription(@PathVariable String id) throws NotFoundException {
        LOGGER.info("POST " + BASE_PATH + "/send-post-request");
        LOGGER.debug("Request Body:\n{}", id);

        restTemplate = new RestTemplate();
        //setting up request for the getPaypal token call
        HttpEntity<MultiValueMap<String, String>> requestEntity = userService.getPaypalAccessToken();

        String cancelUrl = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + id + "/cancel\n";
        //url for retrieving the token
        String apiUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";


        try {

            // Make the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            LOGGER.debug("Request Entity:\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + response);

            String accessToken = userService.parseTokenResponse(response);
            LOGGER.debug("Request Entity:\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + accessToken);

            try {
                //creating the cancel request
                HttpEntity<String> cancelEntity = userService.cancelSubscription(accessToken, id);
                LOGGER.debug("Request Entity:\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + cancelEntity);
                //Actually cancelling
                ResponseEntity<String> cancelled = restTemplate.postForEntity(cancelUrl, cancelEntity, String.class);
                LOGGER.debug("Request Entity:\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + cancelled);

                userService.deleteSubscription(id);


            } catch (Exception e) {
                LOGGER.error("Error making external POST request", e);
                // Handle the exception or return an appropriate response
            }


        } catch (Exception e) {
            LOGGER.error("Error making external POST request", e);
            // Handle the exception or return an appropriate response
        }

    }


}
