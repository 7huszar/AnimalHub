package at.ac.tuwien.sepr.groupphase.backend.service.conflict;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserConflict {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    public void conflictForRegister(String email) throws ConflictException {
        LOGGER.debug("conflictsForRegister({})", email);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            return;
        }

        if (LocalDateTime.now().isBefore(user.getRegistrationStamp().plusMinutes(30))) {
            throw new ConflictException("Could not register user", Collections.singletonList("User already registered"));
        }
    }

    public void conflictForUpdate(UserDetailDto userDetailDto) throws NotAuthorizedException {
        LOGGER.debug("conflictsForUpdate({})", userDetailDto);

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetailDto.getEmail().equals(email)) {
            throw new NotAuthorizedException("Cannot update your own role");
        }
    }





    public void conflictForDelete(UUID id) throws NotAuthorizedException, NotFoundException {
        LOGGER.trace("checkUserDeleteConflicts({})", id);

        if (userRepository.findApplicationUserById(id) == null) {
            throw new NotFoundException("User by id " + id + " does not exist");
        } else {
            ApplicationUser user = userRepository.findApplicationUserById(id);
            if (user.getRole().equals(UserRole.ADMIN)) {
                throw new NotAuthorizedException("Cannot delete another Admin");
            }
        }
    }

}
