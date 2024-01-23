package at.ac.tuwien.sepr.groupphase.backend.unittests.RepositoryTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepr.groupphase.backend.entity.SubscriptionKey;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    @Test
    public void whenCreateSubscription_thenFindById() {
        // Given
        Animal animal = allAnimalEntities.get(0);
        ApplicationUser user = ApplicationUser.builder()
            .registrationStamp(LocalDateTime.now())
            .email("harry@gmail.com")
            .role(UserRole.USER)
            .password("password")
            .build();
        userRepository.save(user);

        SubscriptionKey key = SubscriptionKey.builder()
            .userId(user.getId())
            .animalId(animal.getId())
            .build();

        Subscription subscription = Subscription.builder()
            .user(user)
            .animal(animal)
            .key(key)
            .tier(1)
            .build();


        // When
        subscriptionRepository.save(subscription);

        // Then
        Subscription found = subscriptionRepository.findById(key).orElse(null);
        assertNotNull(found, "Subscription should be found!");
        assertEquals(found.getUser().getId(), user.getId(), "User ID should match!");
        assertEquals(found.getAnimal().getId(), animal.getId(), "Animal ID should match!");
        assertEquals(found.getTier(), 1, "Tier should match!");
    }

}
