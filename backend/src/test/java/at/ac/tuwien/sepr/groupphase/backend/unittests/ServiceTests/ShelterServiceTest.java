package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ShelterServiceTest extends TestBase {

    @Autowired
    ShelterService shelterService;

    @Autowired
    AnimalShelterMapper mapper;

    // function - FIND ALL
    // POSITIVE TEST(S)
    @Test
    public void findAllSuccessfully() throws ValidationException, ConflictException {
        List<ShelterDto> result = shelterService.findAll();

        assertAll(
            () -> assertEquals(result.size(), allShelterEntities.size())
        );
    }

    // function - CREATE
    // POSITIVE TEST(S)
    @Test
    public void createNewShelterSuccessfully() throws ValidationException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
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

    //    NEGATIVE TEST(S)
    @Test
    public void createNewShelterWithInvalidAddressAndPhoneNumberThrowsValidationException() {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address("")
            .phoneNumber("")
            .email(TEST_SHELTER_EMAIL)
            .build();

        assertThrows(ValidationException.class, () -> shelterService.create(createShelterDto));
    }

    // function - UPDATE
    // POSITIVE TEST(S)
    @Test
    public void updateShelterSuccessfully() throws ValidationException, NotFoundException {
        ShelterDto updateValues = ShelterDto.builder()
            .id(allShelterEntities.get(0).getId())
            .name("new name")
            .address("new address")
            .email("new@email.com")
            .phoneNumber("+43 4343 434343")
            .build();

        ShelterDto updatedShelter = shelterService.update(updateValues);

        assertAll(
            () -> assertEquals(updatedShelter.getName(), updateValues.getName()),
            () -> assertEquals(updatedShelter.getAddress(), updateValues.getAddress()),
            () -> assertEquals(updatedShelter.getPhoneNumber(), updateValues.getPhoneNumber()),
            () -> assertEquals(updatedShelter.getEmail(), updateValues.getEmail()),
            () -> assertEquals(updatedShelter.getId(), updateValues.getId())

        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void updateShelterWithNonExistingIdThrowsNotFoundException() {
        ShelterDto updateValues = ShelterDto.builder()
            .id(UUID.randomUUID())
            .name("new name")
            .address("new address")
            .email("new email")
            .phoneNumber("+43 4343 434343")
            .build();

        assertThrows(NotFoundException.class, () -> shelterService.update(updateValues));
    }

    // function - SEARCH
    // POSITIVE TEST(S)

    @Test
    public void searchForNameHerzSuccessfully() {
        List<Shelter> givenSheltersWithHerzInName = allShelterEntities.stream().filter(s -> s.getName().toLowerCase().contains("herz")).toList();
        ShelterSearchDto shelterSearchDto = new ShelterSearchDto("herz", "", "", "");

        List<ShelterListDto> foundSheltersWithHerzInName = shelterService.search(shelterSearchDto);

        assertAll(
            () -> assertEquals(givenSheltersWithHerzInName.size(), foundSheltersWithHerzInName.size()),
            () -> assertEquals(givenSheltersWithHerzInName.get(0).getName(), foundSheltersWithHerzInName.get(0).name()),
            () -> assertEquals(givenSheltersWithHerzInName.get(0).getAddress(), foundSheltersWithHerzInName.get(0).address()),
            () -> assertEquals(givenSheltersWithHerzInName.get(0).getPhoneNumber(), foundSheltersWithHerzInName.get(0).phoneNumber()),
            () -> assertEquals(givenSheltersWithHerzInName.get(0).getEmail(), foundSheltersWithHerzInName.get(0).email())
        );
    }

    @Test
    public void searchForNothingReturnsAllSuccessfully() {
        List<Shelter> givenShelters = allShelterEntities;
        ShelterSearchDto emptySearch = new ShelterSearchDto("", "", "", "");

        List<ShelterListDto> foundShelters = shelterService.search(emptySearch);

        for (int index = 0; index < givenShelters.size(); index++) {
            int tempIndex = index;
            assertAll(
                () -> assertEquals(givenShelters.get(tempIndex).getId(), foundShelters.get(tempIndex).id()),
                () -> assertEquals(givenShelters.get(tempIndex).getName(), foundShelters.get(tempIndex).name()),
                () -> assertEquals(givenShelters.get(tempIndex).getAddress(), foundShelters.get(tempIndex).address()),
                () -> assertEquals(givenShelters.get(tempIndex).getPhoneNumber(), foundShelters.get(tempIndex).phoneNumber()),
                () -> assertEquals(givenShelters.get(tempIndex).getEmail(), foundShelters.get(tempIndex).email())
            );
        }
    }



    // function - FIND BY ID
    // POSITIVE TEST(S)
    @Test
    public void findByIdSuccessfully() throws NotFoundException {

        Shelter givenShelter = allShelterEntities.get(0);

        ShelterDetailDto foundShelter = shelterService.findById(givenShelter.getId());

        assertAll(
            () -> assertEquals(givenShelter.getName(), foundShelter.getName()),
            () -> assertEquals(givenShelter.getAddress(), foundShelter.getAddress()),
            () -> assertEquals(givenShelter.getPhoneNumber(), foundShelter.getPhoneNumber()),
            () -> assertEquals(givenShelter.getEmail(), foundShelter.getEmail())
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void findByNonExistingIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> shelterService.findById(UUID.randomUUID()));
    }

    // function - DELETE
    // POSITIVE TEST(S)
    @Test
    public void deleteShelterSuccessfully() throws ValidationException, NotFoundException {

        UUID shelterId = allShelterEntities.get(0).getId();

        shelterService.delete(shelterId);

        assertAll(
            () -> assertEquals(allShelterEntities.size() - 1, shelterService.findAll().size()),
            () -> assertThrows(NotFoundException.class, () -> shelterService.findById(shelterId))
        );
    }

    //NEGATIVE TEST(S)
    @Test
    public void deleteNonExistingShelterThrowsNotFoundException() throws ValidationException, NotFoundException {
        assertThrows(NotFoundException.class, () -> shelterService.delete(UUID.randomUUID()));
        assertEquals(shelterService.findAll().size(), allShelterEntities.size());
    }


    // function - getProfileImage
    // POSITIVE TEST(S)
    @Test
    public void getProfileImageSuccessfully() throws NotFoundException {
        File originalProfilePicture = allShelterEntities.get(0).getProfilePicture();

        FileDetailDto returnProfilePicture = shelterService.getProfileImage(allShelterEntities.get(0).getId());

        assertAll(
            () -> assertNotNull(returnProfilePicture),
            () -> assertEquals(originalProfilePicture.getContentLength(), returnProfilePicture.getContentLength())
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void getProfileImageWithNonExistingIdThrowsNotFound() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> shelterService.getProfileImage(UUID.randomUUID()));
    }

    // function - setProfileImage
    // POSITIVE TEST(S)
    @Test
    public void setProfileImageSuccessfully() throws NotFoundException, IOException, ConflictException, ValidationException {
        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();
        UUID id = shelterService.create(createShelterDto).getId();
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();

        FileDto returnFile = shelterService.setProfileImage(id, createFileDto);

        assertAll(
            () -> assertNotNull(returnFile),
            () -> assertEquals(returnFile.getName(), createFileDto.getName()),
            () -> assertEquals(returnFile.getSummary(), createFileDto.getSummary()),
            () -> assertEquals(returnFile.getContentMimeType(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())))
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void setProfileImageWhenAlreadySetThrowsConflictException() throws IOException {
        UUID id = allShelterEntities.get(0).getId();
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();

        assertThrows(ConflictException.class, () -> shelterService.setProfileImage(id, createFileDto));
    }

    @Test
    public void setProfileImageWithEmptyImageThrowsValidationException() throws ValidationException, ConflictException {
        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();
        UUID id = shelterService.create(createShelterDto).getId();
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            // NO FILE INPUT
            .build();

        assertThrows(ValidationException.class, () -> shelterService.setProfileImage(id, createFileDto));
    }

    // function - UPDATEPROFILEIMAGE
    // POSITIVE TEST(S)
    @Test
    public void updateProfileImageSuccessfully() throws IOException, ValidationException, NotFoundException {
        UUID id = allShelterEntities.get(0).getId();
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();

        FileDto returnFile = shelterService.updateProfileImage(id, createFileDto);

        assertAll(
            () -> assertNotNull(returnFile),
            () -> assertEquals(returnFile.getName(), createFileDto.getName()),
            () -> assertEquals(returnFile.getSummary(), createFileDto.getSummary()),
            () -> assertEquals(returnFile.getContentMimeType(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())))
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void updateProfileImageForNonExistingShelterThrowsNotFoundException() throws IOException {
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();
        assertThrows(NotFoundException.class, () -> shelterService.updateProfileImage(UUID.randomUUID(), createFileDto));
    }
}