package at.ac.tuwien.sepr.groupphase.backend.unittests.RepositoryTests;


import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@SpringBootTest
@ActiveProfiles("test")
public class ShelterRepositoryTest extends TestBase {

    @Autowired
    private ShelterRepository shelterRepository;


    // function - FIND ALL

    @Test
    public void findAllSuccessfully() {
        List<Shelter> result = shelterRepository.findAll();

        assertAll(
            () -> assertEquals(result.size(), allShelterEntities.size())
        );
    }


    // function - CREATE
    // POSITIVE TEST(S)
    @Transactional
    @Test
    public void createNewShelterWithSuccess() {

        Shelter shelter = Shelter.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

        Shelter createdShelter = shelterRepository.save(shelter);

        assertAll(
            () -> assertEquals(allShelterEntities.size() + 1, shelterRepository.findAll().size()),
            () -> assertEquals(createdShelter.getName(), shelter.getName()),
            () -> assertEquals(createdShelter.getAddress(), shelter.getAddress()),
            () -> assertEquals(createdShelter.getPhoneNumber(), shelter.getPhoneNumber()),
            () -> assertEquals(createdShelter.getEmail(), shelter.getEmail()),
            () -> assertNotNull(createdShelter.getId()),
            () -> assertNotNull(createdShelter.getCreatedOn())
        );
    }

    // NEGATIVE TEST(S)
    @Transactional
    @Test
    public void createNewEmptyShelterUnsuccessfully() {
        Shelter shelter = null;

        assertAll(
            () -> assertThrows(InvalidDataAccessApiUsageException.class, () -> shelterRepository.save(shelter)),
            () -> assertEquals(shelterRepository.findAll().size(), allShelterEntities.size())
        );
    }

    // function - DELETE
    // POSITIVE TEST(S)

    //    @Transactional
    //    @Test
    //    public void deleteShelterSuccessfully() {
    //        Shelter deleteShelter = allShelterEntities.get(0);
    //        shelterRepository.deleteById(deleteShelter.getId());
    //
    //        assertAll(
    //            () -> assertEquals(shelterRepository.findAll().size(), allShelterEntities.size() - 1),
    //            () -> assertFalse(shelterRepository.existsById(deleteShelter.getId()))
    //        );
    //
    //    }
    //
    //    // NEGATIVE TEST(S)
    //    @Transactional
    //    @Test
    //    public void deleteNonExistingShelterUnsuccessfully() {
    //        Shelter nonExistingShelter = Shelter.builder()
    //            .id(UUID.randomUUID())
    //            .name(TEST_SHELTER_NAME)
    //            .address(TEST_SHELTER_ADDRESS)
    //            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
    //            .email(TEST_SHELTER_EMAIL)
    //            .build();
    //
    //        shelterRepository.delete(nonExistingShelter);
    //
    //        assertEquals(shelterRepository.findAll().size(), allShelterEntities.size());
    //
    //    }


}
