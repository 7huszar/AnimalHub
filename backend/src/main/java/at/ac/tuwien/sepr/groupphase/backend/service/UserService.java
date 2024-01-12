package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.CreateSubscriptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.MailRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.PasswordResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address.
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exist
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    ApplicationUser findApplicationUserByEmail(String email) throws NotFoundException;

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are bad
     */
    String login(UserLoginDto userLoginDto, HttpServletRequest request) throws NotFoundException;

    /**
     * Registers a user and sends an e-mail to verify given e-mail.
     * The role of the user is initially set to UserRole.INACTIVE.
     *
     * @param userLoginDto contains e-mail and password
     * @throws ValidationException if e-mail or password are invalid as defined in {@link at.ac.tuwien.sepr.groupphase.backend.service.validator.UserValidator}
     * @throws ConflictException   if user already exists
     */
    void register(UserLoginDto userLoginDto) throws ValidationException, ConflictException;

    void requestMail(MailRequestDto mailRequestDto) throws NotFoundException;

    UserDetailDto getCurrentUser() throws NotFoundException;

    /**
     * Search for users in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in user.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the users where the given fields match.
     */
    List<UserDetailDto> search(UserSearchDto searchParameters);


    /**
     * Delete the user with the given id from the persistent data store.
     *
     * @param id the id of the user to be deleted.
     * @code NotFoundException thrown when the id does not point to any user in the persistent data store.
     */
    void delete(UUID id) throws ConflictException, NotAuthorizedException, NotFoundException;

    /**
     * Verifies e-mail of user given by token claim.
     * More precisely, the status of the role of the user is set from UserRole.INACTIVE to UserRole.USER.
     *
     * @param token Verification token
     * @throws NotFoundException if the token is invalid in any kind
     */
    void verify(String token) throws NotFoundException, JwtException;


    /**
     * Get the user with given ID from the database.
     *
     * @param id user id
     * @return the user with ID {@code id}
     * @code NotFoundException when there is no user with the given ID
     */
    UserDetailDto findById(UUID id) throws NotFoundException;


    /**
     * Updates the user with the data given in {@code userDetailDto} in the persistent data store.
     *
     * @param userDetailDto the user to update
     * @return the updated user
     * @code NotFoundException when there is no user with the given ID
     */
    UserDetailDto update(UserDetailDto userDetailDto) throws ValidationException, NotFoundException;

    /**
     * Sends an e-mail with a token valid to reset a password of an existing user. <br>
     * Languages of user are either Englisch en or German de
     *
     * @param mailRequestDto e-mail of user to reset password and language of mail sent
     * @throws ValidationException if e-mail is not correct
     * @throws NotFoundException   if user does not exist
     */
    void requestPasswordReset(MailRequestDto mailRequestDto) throws ValidationException, NotFoundException, NotAuthorizedException;

    void resetPassword(PasswordResetDto resetDto) throws NotAuthorizedException, NotFoundException, ValidationException;

    List<SubscriptionDetailDto> findAllSubscriptionsByUser(UserDetailDto userDetailDto);

    SubscriptionDetailDto createSubscription(CreateSubscriptionDto createSubscriptionDto);

    /**
     * Delete the subscription with the given id from the persistent data store.
     *
     * @param id the id of the subscription to be deleted.
     */
    void deleteSubscription(String id) throws NotFoundException;

    HttpEntity<MultiValueMap<String, String>> getPaypalAccessToken(UUID shelterId) throws NotFoundException;

    String parseTokenResponse(ResponseEntity<String> response) throws NotFoundException, JsonProcessingException;

    HttpEntity<String> cancelSubscription(String accessToken, String id) throws NotFoundException;

    HttpEntity<Void> checkIfSubscriptionIsActive(String accessToken, String subscriptionId) throws NotFoundException;

    String parseCheckSubscriptionResponse(ResponseEntity<String> response) throws NotFoundException;

    SubscriptionDetailDto isSubscribed(UUID animalId, UUID userId);

    HttpEntity<String> createSubscriptionPlan(String accessToken);


}