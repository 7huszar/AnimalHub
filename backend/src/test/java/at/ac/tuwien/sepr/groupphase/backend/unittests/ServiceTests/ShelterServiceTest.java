package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.TEST_ANIMAL_SHELTER;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.TEST_SHELTER_ADDRESS;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.TEST_SHELTER_EMAIL;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.TEST_SHELTER_NAME;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.TestData.TEST_SHELTER_PHONE_NUMBER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShelterServiceTest {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    private final ShelterService shelterService;

    private final AnimalShelterMapper mapper;

    @BeforeEach
    public void beforeEach() {
        shelterRepository.deleteAll();
        animalRepository.deleteAll();   // at this point there shouldnt be any animals in the repo (just to be sure)
    }

    @Test
    public void findByIdWithSuccess() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);
        ShelterDetailDto foundShelter = shelterService.findById(shelterDetailDto.getId());

        assertAll(
            () -> assertEquals(createShelterDto.getName(), foundShelter.getName()),
            () -> assertEquals(createShelterDto.getAddress(), foundShelter.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), foundShelter.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), foundShelter.getEmail())
        );
    }

    @Test
    public void findByIdUnsuccessfully() {
        assertThrows(NotFoundException.class, () -> shelterService.findById(TEST_ANIMAL_SHELTER.getId()));
    }

    @Test
    public void createNewShelterWithSuccess() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
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
            .address("")
            .phoneNumber("")
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        assertThrows(ValidationException.class, () -> shelterService.create(createShelterDto));
    }

    @Test
    public void deleteShelterWithSuccess() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
            () -> assertEquals(createShelterDto.getName(), shelterDetailDto.getName()),
            () -> assertEquals(createShelterDto.getAddress(), shelterDetailDto.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), shelterDetailDto.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), shelterDetailDto.getEmail()),
            () -> assertEquals(createShelterDto.getCreatedOn(), shelterDetailDto.getCreatedOn())
        );

        shelterService.delete(shelterDetailDto.getId());

        assertEquals(0, shelterService.findAll().size());
    }

    @Test
    public void deleteShelterUnsuccessfully() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
            () -> assertEquals(createShelterDto.getName(), shelterDetailDto.getName()),
            () -> assertEquals(createShelterDto.getAddress(), shelterDetailDto.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), shelterDetailDto.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), shelterDetailDto.getEmail()),
            () -> assertEquals(createShelterDto.getCreatedOn(), shelterDetailDto.getCreatedOn())
        );

        shelterService.delete(shelterDetailDto.getId());
        assertEquals(0, shelterService.findAll().size());

        assertThrows(NotFoundException.class, () -> shelterService.delete(shelterDetailDto.getId()));
    }

    @Test
    public void updateShelterUnsuccessfully() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        ShelterDetailDto updateShelterDto = shelterService.findById(shelterDetailDto.getId());
        updateShelterDto.setName("test");
        updateShelterDto.setAddress("testAddress");
        updateShelterDto.setEmail("testMail@tester.com");
        updateShelterDto.setPhoneNumber("12345");
        updateShelterDto.setCreatedOn(shelterDetailDto.getCreatedOn());

        ShelterDto updatedShelter = shelterService.update(mapper.shelterDetailDtoToShelterDto(updateShelterDto));

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
            () -> assertEquals(updatedShelter.getName(), "test"),
            () -> assertEquals(updatedShelter.getAddress(), "testAddress"),
            () -> assertEquals(updatedShelter.getPhoneNumber(), "12345"),
            () -> assertEquals(updatedShelter.getEmail(), "testMail@tester.com"),
            () -> assertEquals(updatedShelter.getCreatedOn(), shelterDetailDto.getCreatedOn())
        );
    }
}