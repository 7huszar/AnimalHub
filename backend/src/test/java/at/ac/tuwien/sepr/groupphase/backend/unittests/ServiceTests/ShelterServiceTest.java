package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
public class ShelterServiceTest extends TestBase {

    @Autowired
    ShelterService shelterService;

    @Autowired
    AnimalShelterMapper mapper;


    @Test
    public void findByIdWithSuccess() throws ValidationException, NotFoundException {

        Shelter testShelter = allShelterEntities.get(0);

        ShelterDetailDto foundShelter = shelterService.findById(testShelter.getId());

        assertAll(
            () -> assertEquals(testShelter.getName(), foundShelter.getName()),
            () -> assertEquals(testShelter.getAddress(), foundShelter.getAddress()),
            () -> assertEquals(testShelter.getPhoneNumber(), foundShelter.getPhoneNumber()),
            () -> assertEquals(testShelter.getEmail(), foundShelter.getEmail())
        );
    }

    @Test
    public void findByIdUnsuccessfully() {
        assertThrows(NotFoundException.class, () -> shelterService.findById(UUID.randomUUID()));
    }

    @Test
    public void createNewShelterWithSuccess() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .donationId(TEST_SHELTER_DONATION_ID)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        assertAll(
            () -> assertEquals(allShelterEntities.size() + 1, shelterService.findAll().size()),
            () -> assertEquals(createShelterDto.getName(), shelterDetailDto.getName()),
            () -> assertEquals(createShelterDto.getAddress(), shelterDetailDto.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), shelterDetailDto.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), shelterDetailDto.getEmail()),
            () -> assertEquals(createShelterDto.getCreatedOn(), shelterDetailDto.getCreatedOn())
        );
    }

    @Test
    public void createNewShelterUnsuccessfullyInvalidAddressAndPhoneNumber() {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .donationId(TEST_SHELTER_DONATION_ID)
            .donationId(TEST_SHELTER_DONATION_ID)
            .address("")
            .phoneNumber("")
            .email(TEST_SHELTER_EMAIL)
            .build();

        assertThrows(ValidationException.class, () -> shelterService.create(createShelterDto));
    }

    @Test
    public void deleteShelterWithSuccess() throws ValidationException, NotFoundException {

        UUID shelterId = allShelterEntities.get(0).getId();

        shelterService.delete(shelterId);

        assertAll(
            () -> assertEquals(allShelterEntities.size() - 1, shelterService.findAll().size()),
            () -> assertThrows(NotFoundException.class, () -> shelterService.findById(shelterId))
        );
    }

    @Test
    public void deleteShelterUnsuccessfully() throws ValidationException, NotFoundException {
        assertThrows(NotFoundException.class, () -> shelterService.delete(UUID.randomUUID()));
    }

    @Test
    public void updateShelterSuccessfully() throws ValidationException, NotFoundException {

        ShelterDetailDto updateShelterDto = shelterService.findById(allShelterEntities.get(0).getId());
        updateShelterDto.setName("test");
        updateShelterDto.setAddress("testAddress");
        updateShelterDto.setEmail("testMail@tester.com");
        updateShelterDto.setPhoneNumber("12345");

        ShelterDto updatedShelter = shelterService.update(mapper.shelterDetailDtoToShelterDto(updateShelterDto));

        assertAll(
            () -> assertEquals(allShelterEntities.size(), shelterService.findAll().size()),
            () -> assertEquals(updatedShelter.getName(), "test"),
            () -> assertEquals(updatedShelter.getAddress(), "testAddress"),
            () -> assertEquals(updatedShelter.getPhoneNumber(), "12345"),
            () -> assertEquals(updatedShelter.getEmail(), "testMail@tester.com")
        );
    }
}