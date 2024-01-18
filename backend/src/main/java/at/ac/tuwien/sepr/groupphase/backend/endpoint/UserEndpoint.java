package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.CreateSubscriptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = UserEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class UserEndpoint {

    static final String BASE_PATH = "/api/v1/users";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final ShelterService shelterService;
    private final AnimalShelterMapper shelterMapper;
    private RestTemplate restTemplate;


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
    @Operation(summary = "Update specific user in database", security = @SecurityRequirement(name = "apiKey"))
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

    //Deleting subscription from database
    @Secured(UserRole.Values.USER_VALUE)
    @DeleteMapping("/current/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable String id) throws NotFoundException {
        LOGGER.info("DELETE Subscription " + BASE_PATH + "/current/" + id);
        LOGGER.debug("delete({})", id);

        userService.deleteSubscription(id);
    }

    //Canceling subscription from paypal
    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/paypal/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelSubscription(@PathVariable String id, @RequestBody String shelterId) throws NotFoundException, JsonProcessingException {
        LOGGER.info("POST " + BASE_PATH + "/send-post-request");
        LOGGER.debug("Request Body:\n{}", id);

        // Create an ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON string
        JsonNode jsonNode = objectMapper.readTree(shelterId);

        // Extract shelterId
        String shelterIdString = jsonNode.path("body").path("shelterId").asText();
        LOGGER.debug("Extracted shelterId: " + shelterIdString);

        UUID shelterIdUuid = UUID.fromString(shelterIdString);
        System.out.println("Parsed UUID: " + shelterIdUuid);

        if (!checkIfSubscriptionActive(id, shelterIdUuid).equals("is active")) {
            LOGGER.error("The Subscription you tried to cancel was not active");
            throw new NotFoundException("The subscription you tried to cancel Was not active");
        }

        restTemplate = new RestTemplate();
        //setting up request for the getPaypal token call
        HttpEntity<MultiValueMap<String, String>> requestEntity = userService.getPaypalAccessToken(shelterIdUuid);

        String cancelUrl = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + id + "/cancel\n";
        //url for retrieving the token
        String apiUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";


        try {

            // Make the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            String accessToken = userService.parseTokenResponse(response);

            try {
                //creating the cancel request
                HttpEntity<String> cancelEntity = userService.cancelSubscription(accessToken, id);
                //Actually cancelling
                ResponseEntity<String> cancelled = restTemplate.postForEntity(cancelUrl, cancelEntity, String.class);

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

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/paypal/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String checkIfSubscriptionActive(@PathVariable String id, @RequestBody UUID shelterId) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("Request Body:\n{}", id);

        restTemplate = new RestTemplate();
        //setting up request for the getPaypal token call
        HttpEntity<MultiValueMap<String, String>> requestEntity = userService.getPaypalAccessToken(shelterId);


        //url for retrieving the token
        String apiUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";
        String checkUrl = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + id;


        try {

            // Make the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            String accessToken = userService.parseTokenResponse(response);

            try {
                //creating the checking request
                HttpEntity<Void> checkEntity = userService.checkIfSubscriptionIsActive(accessToken, id);

                //Actually checking
                ResponseEntity<String> checkResponse = restTemplate.exchange(checkUrl, HttpMethod.GET, checkEntity, String.class);
                //extracting the active or inactive part
                String isActive = userService.parseCheckSubscriptionResponse(checkResponse);


                if (Objects.equals(isActive, "ACTIVE")) {
                    LOGGER.info("Subscription is active");
                    return "is active";
                } else if (Objects.equals(isActive, "INACTIVE")) {
                    LOGGER.info("Subscription is active");
                    this.userService.deleteSubscription(id);
                    return "is inactive";
                } else {
                    // Handle other status codes accordingly
                    System.out.println("Unexpected status code: ");
                    return null;
                }


            } catch (Exception e) {
                LOGGER.error("Error making external POST request", e);
                return null;
                // Handle the exception or return an appropriate response
            }


        } catch (Exception e) {
            LOGGER.error("Error making external POST request", e);
            return null;
        }
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check if current User is subscribed", security = @SecurityRequirement(name = "apiKey"))
    public SubscriptionDetailDto isSubscribed(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("animalId({})", id);


        UserDetailDto user = userService.getCurrentUser();
        SubscriptionDetailDto subscription = userService.isSubscribed(id, user.getId());

        if (subscription == null) {
            return null;
        }

        if (!checkIfSubscriptionActive(subscription.getSubscriptionId(), subscription.getAnimal().getShelter().getId()).equals("is active")) {

            LOGGER.error("The Subscription you tried to load was not active");
        }

        return userService.isSubscribed(id, user.getId());
    }


    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check if current User is subscribed", security = @SecurityRequirement(name = "apiKey"))
    public String createSubscriptionPlan(@PathVariable UUID id) throws NotFoundException, JsonProcessingException, ValidationException {
        LOGGER.info("Post " + BASE_PATH);
        LOGGER.debug("shelter({})", id);


        restTemplate = new RestTemplate();

        String apiUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";
        String creatingUrl = "https://api-m.sandbox.paypal.com/v1/billing/plans";

        ResponseEntity<String> responseEntity;
        // Obtain the PayPal access token
        HttpEntity<MultiValueMap<String, String>> requestEntity = userService.getPaypalAccessToken(id);


        try {

            // Make the POST request to get access token
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);


            //parsing so just the access Token is being returned
            String accessToken = userService.parseTokenResponse(response);

            LOGGER.debug("Retrieved Access Token" + accessToken);


            try {
                HttpEntity<String> createSubscriptionPlan = userService.createSubscriptionPlan(accessToken);


                // Make the HTTP POST request to create the subscription plan
                responseEntity = restTemplate.exchange(
                    creatingUrl,
                    HttpMethod.POST,
                    createSubscriptionPlan,
                    String.class
                );


            } catch (Exception e) {
                LOGGER.error("Error making external POST request", e);
                return "error creating plan";
                // Handle the exception or return an appropriate response
            }


        } catch (Exception e) {
            LOGGER.error("Are u sure you provided the correct credentials", e);
            // Handle the exception or return an appropriate response
            return "Incorrect credentials";
        }

        // Check if the request was successful (HTTP status code 2xx)
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            //Extracting SubscriptionPLanId
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Extract the subscription plan ID (adjust the path according to the actual structure of the response)
            String subscriptionPlanId = jsonNode.path("id").asText();

            // Now you can use the subscriptionPlanId or handle it as needed
            LOGGER.debug("successfully created plan");

            ShelterDto shelterDto = shelterMapper.shelterDetailDtoToShelterDto(shelterService.findById(id));
            shelterDto.setSubscriptionId(subscriptionPlanId);

            LOGGER.debug("ShelterDto that is being updated" + shelterDto);
            LOGGER.info("Updating Sheltern with subscription Plan");
            shelterService.update(shelterDto);

            return (subscriptionPlanId);
        } else {

            LOGGER.debug("Failed to create subscription plan. Status code: " + responseEntity.getStatusCode());
            //TODO make all of these returns proper error messsages
            return "Could not create Subscription Plan please try again Later";
        }

    }


    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("subscriptions/{id}")
    @Operation(summary = "Update specific shelter in database", security = @SecurityRequirement(name = "apiKey"))
    public void updatePaypalDetails(@PathVariable UUID id, @RequestBody ShelterDetailDto shelterDetailDto) throws NotFoundException, NotAuthorizedException, ValidationException, JsonProcessingException, ConflictException {
        LOGGER.info("PUT " + BASE_PATH + "/subscriptions/" + id);
        LOGGER.debug("update Paypal credentials ({})", id);


        ShelterDetailDto shelter = shelterService.findById(id);


        //TODO make this a proper validation

        List<String> conflictErrors = new ArrayList<>();

        if (shelter.getPaypalClientId().equals(shelterDetailDto.getPaypalClientId()) || shelter.getPaypalSecret().equals(shelterDetailDto.getPaypalSecret())) {
            conflictErrors.add("The provided Client Id or Secret are already defined");
        }

        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Creating Animal creates conflict error", conflictErrors);
        }


        userService.updatePaypalCredentials(id, shelterDetailDto);

        restTemplate = new RestTemplate();
        String apiUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";

        HttpEntity<MultiValueMap<String, String>> requestEntity = userService.getPaypalAccessToken(id);


        try {

            // Make the POST request to get access token
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);


            //parsing so just the access Token is being returned
            String accessToken = userService.parseTokenResponse(response);

            LOGGER.debug("Retrieved Access Token" + accessToken);


        } catch (Exception e) {
            userService.updatePaypalCredentials(id, shelter);
            LOGGER.error("Are u sure you provided the correct credentials", e);
            throw new NotAuthorizedException("Provided invalid Paypal credentials. Please make sure you provided the correct Client id and secret");
        }


        String createSubscription = createSubscriptionPlan(id);
        shelterDetailDto.setSubscriptionId(createSubscription);
        shelterService.update(shelterMapper.shelterDetailDtoToShelterDto(shelterDetailDto));


    }

}
