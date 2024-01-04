package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ShelterRepository shelterRepository;


    public void generateUsers() {
        List<Shelter> allShelters = shelterRepository.findAll();
        if (!userRepository.findAll().isEmpty()) {
            LOGGER.debug("users already exist");
        } else {
            final var admin = ApplicationUser.builder()
                .email("admin@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ADMIN)
                .build();

            final var member1 = ApplicationUser.builder()
                .email("member1@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.MEMBER)
                .shelter(allShelters.get(0))
                .build();

            final var member2 = ApplicationUser.builder()
                .email("member2@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.MEMBER)
                .shelter(allShelters.get(1))
                .build();

            final var member3 = ApplicationUser.builder()
                .email("member3@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.MEMBER)
                .shelter(allShelters.get(2))
                .build();

            final var user = ApplicationUser.builder()
                .email("user@animalhub")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.USER)
                .build();

            LOGGER.debug("saving user {}", admin);
            userRepository.save(admin);

            LOGGER.debug("saving user {}", member1);
            userRepository.save(member1);

            LOGGER.debug("saving user {}", member2);
            userRepository.save(member2);

            LOGGER.debug("saving user {}", member3);
            userRepository.save(member3);

            LOGGER.debug("saving user {}", user);
            userRepository.save(user);
        }
    }
}
