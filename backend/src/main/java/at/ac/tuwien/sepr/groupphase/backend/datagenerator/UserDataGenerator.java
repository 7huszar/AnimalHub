package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@RequiredArgsConstructor
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void generateUsers() {
        if (!userRepository.findAll().isEmpty()) {
            LOGGER.debug("users already exist");
        } else {
            final var admin = ApplicationUser.builder()
                .email("admin@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ADMIN)
                .build();

            final var member = ApplicationUser.builder()
                .email("member@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.MEMBER)
                .build();

            final var user = ApplicationUser.builder()
                .email("user@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.USER)
                .build();

            LOGGER.debug("saving user {}", admin);
            userRepository.save(admin);

            LOGGER.debug("saving user {}", member);
            userRepository.save(member);

            LOGGER.debug("saving user {}", user);
            userRepository.save(user);
        }
    }
}
