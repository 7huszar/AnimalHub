package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.ConflictValidator;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserValidator userValidator;
    private final ConflictValidator conflictChecker;
    private final UserMapper userMapper;


    /**
     * Search for users in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in user.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the users where the given fields match.
     */
    @Override
    public List<UserDetailDto> search(UserSearchDto searchParameters) {

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
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(applicationUser.getRole().getPermissions());

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
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
        LOGGER.debug("register user by email");
        userValidator.validateUserCredentials(userLoginDto);
        conflictChecker.checkRegisterConflicts(userLoginDto.getEmail());
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(userLoginDto.getEmail());
        message.setSubject("Animalhub e-mail confirmation");
        message.setText("http://localhost:4200/#/register/confirm/" + jwtTokenizer.getActivationToken(userLoginDto.getEmail()));
        emailSender.send(message);
         */

        ApplicationUser user = ApplicationUser.builder()
            .email(userLoginDto.getEmail())
            .password(passwordEncoder.encode(userLoginDto.getPassword()))
            .role(UserRole.USER)
            .build();

        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) throws ConflictException {
        LOGGER.debug("Delete Shelter with id: " + id);

        conflictChecker.checkUserDeleteConflicts(id);
        var shelter = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );
        userRepository.delete(shelter);
    }

    @Override
    public UserDetailDto update(UserDetailDto userDetailDto) throws ValidationException {
        LOGGER.debug("Update Shelter with id: " + userDetailDto.getId() + ", with following data:" + userDetailDto);

        userValidator.validateUserCredentialsForUpdate(userDetailDto);
        ApplicationUser oldEntity = userRepository.findById(userDetailDto.getId()).orElseThrow(
            () -> new NotFoundException("User does not exist.")
        );
        oldEntity.setRole(userDetailDto.getRole());

        return userMapper.applicationUserToUserDetailDto(userRepository.save(oldEntity));
    }


    @Override
    public UserDetailDto findById(UUID id) {
        LOGGER.debug("Get User with id: " + id);

        var user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        return userMapper.applicationUserToUserDetailDto(user);
    }
}
