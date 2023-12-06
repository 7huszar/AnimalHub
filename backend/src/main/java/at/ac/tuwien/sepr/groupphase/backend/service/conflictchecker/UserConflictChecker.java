package at.ac.tuwien.sepr.groupphase.backend.service.conflictchecker;

import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@RequiredArgsConstructor
public class UserConflictChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    public void checkRegisterConflicts(String email) throws ConflictException {
        LOGGER.debug("checkRegisterConflicts({})", email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Application user already registered by the name %s".formatted(email));
        }
    }
}
