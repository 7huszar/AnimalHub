package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.CreateSubscriptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.MailRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepr.groupphase.backend.entity.SubscriptionKey;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.mail.MailService;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.conflict.UserConflict;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.UserValidator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserValidator userValidator;
    private final SecurityProperties securityProperties;
    private final UserMapper userMapper;
    private final UserConflict conflict;
    private final MailService mail;


    /**
     * Search for users in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in user.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the users where the given fields match.
     */
    @Override
    public List<UserDetailDto> search(UserSearchDto searchParameters) {
        LOGGER.trace("search({})", searchParameters);

        List<ApplicationUser> users = new ArrayList<>();

        if (searchParameters.email() == null || searchParameters.email().isEmpty()) {
            if (searchParameters.role() == null) {
                users = userRepository.findAll();
            } else {
                users = userRepository.findApplicationUsersByRole(searchParameters.role());
            }

        } else {

            var foundUsers = userRepository.findUsersByEmailIgnoreCaseContaining(searchParameters.email());
            if (searchParameters.role() == null) {
                users = foundUsers;
            } else {
                for (ApplicationUser foundUser : foundUsers) {
                    if (userRepository.findApplicationUsersByRoleAndIdEquals(searchParameters.role(), foundUser.getId()) != null) {
                        users.add(userRepository.findApplicationUsersByRoleAndIdEquals(searchParameters.role(), foundUser.getId()));
                    }
                }
            }
        }
        return users.stream()
            .map(userMapper::applicationUserToUserDetailDto).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.trace("loadUserByUsername({})", email);
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(applicationUser.getRole().getPermissions());

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) throws NotFoundException {
        LOGGER.trace("findApplicationUserByEmail({})", email);
        ApplicationUser applicationUser = userRepository.findByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        LOGGER.trace("login({})", userLoginDto.getEmail());
        // TODO DaoAuthenticationProvider and User for handling everything as UserDetails
        // tmp solution
        ApplicationUser applicationUser = userRepository.findByEmail(userLoginDto.getEmail());
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && applicationUser.getRole() != UserRole.INACTIVE
            && passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Username or password is incorrect or account is locked");
    }

    @Override
    public void register(UserLoginDto userLoginDto) throws ValidationException, ConflictException {
        LOGGER.trace("register({})", userLoginDto.getEmail());

        userValidator.validateUserCredentials(userLoginDto);
        conflict.conflictForRegister(userLoginDto.getEmail());

        LocalDateTime regStamp = LocalDateTime.now();

        ApplicationUser user = ApplicationUser.builder()
            .email(userLoginDto.getEmail())
            .password(passwordEncoder.encode(userLoginDto.getPassword()))
            .role(UserRole.INACTIVE)
            .registrationStamp(regStamp)
            .build();

        if (userRepository.existsByEmail(user.getEmail())) {
            ApplicationUser old = userRepository.findByEmail(user.getEmail());
            old.setPassword(user.getPassword());
            old.setRegistrationStamp(user.getRegistrationStamp());
            user = old;
        }
        userRepository.save(user);
    }

    @Override
    public void requestMail(MailRequestDto mailRequestDto) throws NotFoundException {
        LOGGER.trace("requestMail({})", mailRequestDto);

        ApplicationUser user = userRepository.findByEmail(mailRequestDto.email());
        if (user == null) {
            throw new NotFoundException("user by email " + mailRequestDto.email() + " dosn't exist");
        }
        mail.sendVerificationMail(user.getEmail(), mailRequestDto.lang(), user.getRegistrationStamp());
    }

    @Transactional
    @Override
    public void verify(String token) throws NotFoundException, JwtException {
        LOGGER.trace("verify({})", token);

        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(securityProperties.getJwtSecret().getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String email = claims.get("email", String.class);
        String regStamp = claims.get("regstamp", String.class);
        String lang = claims.get("language", String.class);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("user by e-mail " + email + " ");
        }
        if (!regStamp.equals(user.getRegistrationStamp().toString())) {
            // let's handle the timestamp as a key (which it isn't)
            throw new NotFoundException("verification time expired");
        }
        userRepository.updateRole(email, UserRole.USER);
    }

    @Override
    public UserDetailDto getCurrentUser() throws NotFoundException {
        LOGGER.trace("getCurrentUser()");
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException("User could not be found.");
        }

        return userMapper.applicationUserToUserDetailDto(user);
    }

    @Override
    public void delete(UUID id) throws ConflictException, NotAuthorizedException, NotFoundException {
        LOGGER.debug("delete({})", id);

        conflict.conflictForDelete(id);
        var shelter = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );
        userRepository.delete(shelter);
    }

    @Override
    public UserDetailDto update(UserDetailDto userDetailDto) throws ValidationException, NotFoundException {
        LOGGER.debug("update({})", userDetailDto);

        userValidator.validateUserCredentialsForUpdate(userDetailDto);
        ApplicationUser oldEntity = userRepository.findById(userDetailDto.getId()).orElseThrow(
            () -> new NotFoundException("User does not exist.")
        );
        oldEntity.setRole(userDetailDto.getRole());

        return userMapper.applicationUserToUserDetailDto(userRepository.save(oldEntity));
    }


    @Override
    public UserDetailDto findById(UUID id) throws NotFoundException {
        LOGGER.trace("findById({})", id);

        var user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter by id " + id + " does not exist.")
        );

        return userMapper.applicationUserToUserDetailDto(user);
    }

    @Override
    public void requestPasswordReset(MailRequestDto resetDto) throws ValidationException, NotFoundException, NotAuthorizedException {
        LOGGER.trace("requestPasswordReset({})", resetDto);

        userValidator.validatePasswordRequestResetDto(resetDto);

        ApplicationUser user = userRepository.findByEmail(resetDto.email());

        if (user == null) {
            throw new NotFoundException("User by e-mail " + resetDto.email() + " does not exist");
        }
        if (user.getRole().equals(UserRole.INACTIVE)) {
            throw new NotAuthorizedException("User by e-mail " + user.getEmail() + " is not active yet");
        }

        mail.sendResetPasswordMail(resetDto.email(), resetDto.lang());
    }

    @Transactional
    @Override
    public void resetPassword(PasswordResetDto resetDto) throws JwtException, NotFoundException, ValidationException {
        LOGGER.trace("resetPassword({})", resetDto.token());
        userValidator.validatePasswordResetDto(resetDto);

        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(securityProperties.getJwtSecret().getBytes()))
            .build()
            .parseSignedClaims(resetDto.token())
            .getPayload();

        String email = claims.get("email", String.class);

        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("User by email " + email + " does not exist");
        }

        userRepository.updatePassword(email, passwordEncoder.encode(resetDto.password()));

    }


    @Override
    public SubscriptionDetailDto createSubscription(CreateSubscriptionDto createSubscriptionDto) {

        LOGGER.trace("Create new Subscription: " + createSubscriptionDto);

        Subscription subscription = userMapper.createSubscriptionDtoToSubscription(createSubscriptionDto);

        String id = createSubscriptionDto.getSubscriptionId();
        subscription.setSubscriptionId(id);
        SubscriptionKey key = new SubscriptionKey(subscription.getAnimal().getId(), subscription.getUser().getId());
        subscription.setKey(key);

        Subscription ret = subscriptionRepository.save(subscription);

        return userMapper.subscriptionToSubscriptionDetailDto(ret);
    }


    @Override
    public List<SubscriptionDetailDto> findAllSubscriptionsByUser(UserDetailDto userDetailDto) {
        LOGGER.trace("Find all Subscriptions of User");

        return subscriptionRepository.findAllByUserId(userDetailDto.getId()).stream()
            .map(userMapper::subscriptionToSubscriptionDetailDto)
            .collect(Collectors.toList());
    }


    @Override
    public void deleteSubscription(String id) throws NotFoundException {
        LOGGER.debug("delete({})", id);

        Subscription subscription = subscriptionRepository.findBySubscriptionId(id);
        if (subscription == null) {
            throw new NotFoundException("Subscription could not be found");
        }

        subscriptionRepository.delete(subscription);
    }

    @Override
    public HttpEntity<MultiValueMap<String, String>> getPaypalAccessToken(UUID shelterId) throws NotFoundException {
        LOGGER.debug("get Token");


        Optional<Shelter> shelter = shelterRepository.findById(shelterId);
        Shelter foundShelter = null;

        if (shelter.isPresent()) {
            foundShelter = shelter.get();
            // Use shelter here
        } else {
            throw new NotFoundException("Could not find the Paypal ID");
        }


        String clientId = foundShelter.getPaypalClientId();
        String secret = foundShelter.getPaypalSecret();

        String credentials = clientId + ":" + secret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Setting header for getPaypalAccessTokenRequest
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + base64Credentials);

        // Setting body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        // Creating Request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        LOGGER.debug("Request Entity:\n" + requestEntity);

        return requestEntity;
    }


    @Override
    public String parseTokenResponse(ResponseEntity<String> response) throws NotFoundException {
        LOGGER.debug("Parsing response");

        // Extract the access token from the response body
        String responseBody = response.getBody();

        if (responseBody != null && !responseBody.isEmpty()) {
            try {
                JsonFactory factory = new JsonFactory();
                JsonParser parser = factory.createParser(responseBody);

                // Traverse the JSON structure
                while (parser.nextToken() != null) {
                    if (JsonToken.FIELD_NAME.equals(parser.getCurrentToken()) && "access_token".equals(parser.getText())) {
                        // Move to the value of the "access_token" field
                        parser.nextToken();
                        String accessToken = parser.getText();
                        LOGGER.info("Received access token from Paypal API: {}", accessToken);
                        return accessToken;
                    }
                }

                // If the loop completes without finding "access_token"
                throw new NotFoundException("No 'access_token' found in the response");
            } catch (Exception e) {
                LOGGER.error("Error parsing JSON response: {}", e.getMessage());
                throw new NotFoundException("Error parsing JSON response");
            }
        } else {
            LOGGER.error("Empty or null response body");
            throw new NotFoundException("Access token not found in the response or response body is empty");
        }
    }


    @Override
    public HttpEntity<String> cancelSubscription(String accessToken, String subscriptionId) throws NotFoundException {
        LOGGER.debug("Canceling subscription");

        // Setting headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Accept", "application/json");

        // Setting JSON body
        String jsonBody = "{\"reason\": \"cancelled subscription\"}";

        // Creating Request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        return requestEntity;
    }


    @Override
    public HttpEntity<Void> checkIfSubscriptionIsActive(String accessToken, String subscriptionId) throws NotFoundException {
        LOGGER.debug("Checking if subscription is active");

        //creating headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Creating Request entity with headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return requestEntity;
    }


    @Override
    public String parseCheckSubscriptionResponse(ResponseEntity<String> response) throws NotFoundException {
        LOGGER.debug("Parsing response");

        // Extract the status from the response body
        String responseBody = response.getBody();

        if (responseBody != null && !responseBody.isEmpty()) {
            try {
                JsonFactory factory = new JsonFactory();
                JsonParser parser = factory.createParser(responseBody);

                // Traverse the JSON structure
                while (parser.nextToken() != null) {
                    if (JsonToken.FIELD_NAME.equals(parser.getCurrentToken()) && "status".equals(parser.getText())) {
                        // Move to the value of the "status" field
                        parser.nextToken();
                        String status = parser.getText();
                        LOGGER.info("Received subscription status from PayPal API: {}", status);

                        // Check if the subscription is active
                        if ("ACTIVE".equalsIgnoreCase(status)) {
                            LOGGER.info("Subscription is active");
                            // Additional logic or processing for an active subscription
                        } else {
                            LOGGER.info("Subscription is not active. Status: {}", status);

                        }

                        return status;
                    }
                }

                // If the loop completes without finding "status"
                throw new NotFoundException("No 'status' found in the response");
            } catch (Exception e) {
                LOGGER.error("Error parsing JSON response: {}", e.getMessage());
                throw new NotFoundException("Error parsing JSON response");
            }
        } else {
            LOGGER.error("Empty or null response body");
            throw new NotFoundException("Subscription status not found in the response or response body is empty");
        }

    }


    @Override
    public SubscriptionDetailDto isSubscribed(UUID animalId, UUID userId) {
        LOGGER.trace("Check if user is subscribed");
        SubscriptionKey key = new SubscriptionKey(animalId, userId);
        return userMapper.subscriptionToSubscriptionDetailDto(subscriptionRepository.findByKey(key));
    }


    @Override
    public HttpEntity<String> createSubscriptionPlan(String accessToken) {
        LOGGER.trace("Creating a new Subscription Plan");


        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestBody = "{ "
            + "\"product_id\": \"PROD-0WX22620KG452370H\", "
            + "\"name\": \"Created Automatically\", "
            + "\"description\": \"This is a test\", "
            + "\"billing_cycles\": [ "
            + "{ "
            + "\"frequency\": { \"interval_unit\": \"MONTH\", \"interval_count\": 1 }, "
            + "\"tenure_type\": \"REGULAR\", "
            + "\"sequence\": 1, "
            + "\"total_cycles\": 12, "
            + "\"pricing_scheme\": { \"fixed_price\": { \"value\": \"9.99\", \"currency_code\": \"USD\" } } "
            + "} "
            + "], "
            + "\"payment_preferences\": { \"auto_bill_outstanding\": true, "
            + "\"setup_fee\": { \"value\": \"0.00\", \"currency_code\": \"USD\" } } "
            + "}";


        // Create the HTTP entity with headers and body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        LOGGER.debug("created Request" + String.valueOf(requestEntity));

        return requestEntity;
    }


}

