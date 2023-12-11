package at.ac.tuwien.sepr.groupphase.backend.unittests.RepositoryTests;


import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class ShelterRepositoryTest {

    @Autowired
    private ShelterRepository shelterRepository;

    @Transactional
    @Test
    public void createNewShelterWithSuccess() {

        Shelter shelter = Shelter.builder()
            .id(TEST_SHELTER_ID)
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

            Shelter createdShelter = shelterRepository.save(shelter);

        assertAll(
            () -> assertEquals(1, shelterRepository.findAll().size()),
            () -> assertEquals(createdShelter.getName(), shelter.getName()),
            () -> assertEquals(createdShelter.getAddress(), shelter.getAddress()),
            () -> assertEquals(createdShelter.getPhoneNumber(), shelter.getPhoneNumber()),
            () -> assertEquals(createdShelter.getEmail(), shelter.getEmail()),
            () -> assertNotNull(createdShelter.getId()),
            () -> assertNotNull(createdShelter.getCreatedOn())
        );
    }
}
