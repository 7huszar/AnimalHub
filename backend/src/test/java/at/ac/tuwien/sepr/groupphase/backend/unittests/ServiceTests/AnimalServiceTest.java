package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
public class AnimalServiceTest extends TestBase {

    @Autowired
    AnimalService animalService;
    @Autowired
    AnimalShelterMapper mapper;


    // function - FIND ALL
    // POSITIVE TEST(S)
    @Test
    public void findAllSuccessfully() {
        List<AnimalDetailDto> result = animalService.findAll();

        assertAll(
            () -> assertEquals(result.size(), allAnimalEntities.size())
        );
    }

    // function - CREATE
    // POSITIVE TEST(S)
    @Test
    public void createNewAnimalWithSuccess() {

        CreateAnimalDto createAnimalDto = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(mapper.shelterToShelterDto(allShelterEntities.get(0)))
            .build();

        assertDoesNotThrow(() -> {
            // might throw ValidationException, ConflictException
            AnimalDetailDto returnAnimalDto = animalService.create(createAnimalDto);

            assertAll(
                () -> assertEquals(createAnimalDto.getName(), returnAnimalDto.getName()),
                () -> assertEquals(createAnimalDto.getSpecies(), returnAnimalDto.getSpecies()),
                () -> assertEquals(createAnimalDto.getRace(), returnAnimalDto.getRace()),
                () -> assertEquals(createAnimalDto.getBirthDate(), returnAnimalDto.getBirthDate()),
                () -> assertEquals(createAnimalDto.getHeight(), returnAnimalDto.getHeight()),
                () -> assertEquals(createAnimalDto.getWeight(), returnAnimalDto.getWeight()),
                () -> assertEquals(createAnimalDto.getShelter().getId(), returnAnimalDto.getShelter().getId())
            );

        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void createNewAnimalUnsuccessfullyNonExistingShelter() {

        Shelter nonExistingShelter = Shelter.builder()
            .id(UUID.randomUUID())
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

        CreateAnimalDto animal = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(mapper.shelterToShelterDto(nonExistingShelter)) // Using a non-existent shelter
            .build();

        assertThrows(ConflictException.class, () -> animalService.create(animal));
    }

    @Test
    public void createNewAnimalUnsuccessfullyInvalidNameAndWeight() {

        CreateAnimalDto animalDto = CreateAnimalDto.builder()
            .name("")
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(0)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(mapper.shelterToShelterDto(allShelterEntities.get(1)))
            .build();

        assertThrows(ValidationException.class, () -> animalService.create(animalDto));
    }

    // function - UPDATE
    // POSITIVE TEST(S)
    @Test
    public void updateAnimalSuccessfully() {
        AnimalDetailDto updateValues = AnimalDetailDto.builder()
            .id(allAnimalEntities.get(0).getId())
            .name("neuer Name")
            .birthDate(LocalDate.of(2000, 01, 01))
            .species(Species.CAT)
            .race("updated race")
            .height(1)
            .weight(2)
            .shelter(mapper.shelterToShelterDto(allAnimalEntities.get(0).getShelter()))
            .build();

        assertDoesNotThrow(() -> {
            // might throw ValidationException, NotFoundException
            AnimalDetailDto updatedAnimal = animalService.update(updateValues);

            assertAll(
                () -> assertEquals(updatedAnimal.getName(), updateValues.getName()),
                () -> assertEquals(updatedAnimal.getSpecies(), updateValues.getSpecies()),
                () -> assertEquals(updatedAnimal.getRace(), updateValues.getRace()),
                () -> assertEquals(updatedAnimal.getBirthDate(), updateValues.getBirthDate()),
                () -> assertEquals(updatedAnimal.getHeight(), updateValues.getHeight()),
                () -> assertEquals(updatedAnimal.getWeight(), updateValues.getWeight()),
                () -> assertEquals(updatedAnimal.getShelter().getId(), updateValues.getShelter().getId())
            );

        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void updateAnimalWithNonExistingIdThrowsNotFoundException() {
        AnimalDetailDto updateValues = AnimalDetailDto.builder()
            // RANDOM ID
            .id(UUID.randomUUID())
            // RANDOM ID
            .name("neuer Name")
            .birthDate(LocalDate.of(2000, 01, 01))
            .species(Species.CAT)
            .race("updated race")
            .height(1)
            .weight(2)
            .shelter(mapper.shelterToShelterDto(allAnimalEntities.get(0).getShelter()))
            .build();

        assertThrows(NotFoundException.class, () -> animalService.update(updateValues));

    }

    @Test
    public void updateAnimalWithEmptyNameThrowsValidationException() {
        AnimalDetailDto updateValues = AnimalDetailDto.builder()
            .id(allAnimalEntities.get(0).getId())
            // EMPTY NAME
            .name("")
            // EMPTY NAME
            .birthDate(LocalDate.of(2000, 01, 01))
            .species(Species.CAT)
            .race("updated race")
            .height(1)
            .weight(2)
            .shelter(mapper.shelterToShelterDto(allAnimalEntities.get(0).getShelter()))
            .build();

        assertThrows(ValidationException.class, () -> animalService.update(updateValues));

    }

    // function - FINDBYID

    @Test
    public void findByIdSuccessfully() {
        AnimalDetailDto expectedAnimal = mapper.animalToAnimalDetailDto(allAnimalEntities.get(1));

        assertDoesNotThrow(() -> {
            // might throw NotFoundException
            AnimalDetailDto foundAnimal = animalService.findById(expectedAnimal.getId());

            assertAll(
                () -> assertEquals(expectedAnimal.getName(), foundAnimal.getName()),
                () -> assertEquals(expectedAnimal.getSpecies(), foundAnimal.getSpecies()),
                () -> assertEquals(expectedAnimal.getRace(), foundAnimal.getRace()),
                () -> assertEquals(expectedAnimal.getBirthDate(), foundAnimal.getBirthDate()),
                () -> assertEquals(expectedAnimal.getHeight(), foundAnimal.getHeight()),
                () -> assertEquals(expectedAnimal.getWeight(), foundAnimal.getWeight()),
                () -> assertEquals(expectedAnimal.getShelter().getId(), foundAnimal.getShelter().getId())
            );

        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void findByNonExistingIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> animalService.findById(UUID.randomUUID()));
    }

    // function - DELETE
    // POSITIVE TEST(S)
    @Test
    public void deleteAnimalSuccessfully() {

        UUID animalId = allAnimalEntities.get(0).getId();

        // might throw NotFoundException
        assertDoesNotThrow(() -> animalService.delete(animalId));

        assertAll(
            () -> assertEquals(allAnimalEntities.size() - 1, animalService.findAll().size()),
            () -> assertThrows(NotFoundException.class, () -> animalService.findById(animalId))
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void deleteNonExistingAnimalThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> animalService.delete(UUID.randomUUID()));
    }

    // function - getProfileImage
    // POSITIVE TEST(S)
    @Test
    public void getProfileImageSuccessfully() {
        File originalProfilePicture = allAnimalEntities.get(0).getProfilePicture();

        assertDoesNotThrow(() -> {
            // might throw NotFoundException
            FileDetailDto returnProfilePicture = animalService.getProfileImage(allAnimalEntities.get(0).getId());

            assertAll(
                () -> assertNotNull(returnProfilePicture),
                () -> assertEquals(originalProfilePicture.getContentLength(), returnProfilePicture.getContentLength())
            );
        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void getProfileImageWithNonExistingIdThrowsNotFound() {
        assertThrows(NotFoundException.class, () -> animalService.getProfileImage(UUID.randomUUID()));
    }

    // function - setProfileImage
    // POSITIVE TEST(S)
    @Test
    public void setProfileImageSuccessfully() {

        assertDoesNotThrow(() -> {
            CreateAnimalDto createAnimalDto = CreateAnimalDto.builder()
                .name(TEST_ANIMAL_NAME)
                .species(TEST_ANIMAL_SPECIES)
                .race(TEST_ANIMAL_RACE)
                .birthDate(TEST_ANIMAL_BIRTH_DATE)
                .height(TEST_ANIMAL_HEIGHT)
                .weight(TEST_ANIMAL_WEIGHT)
                .shelter(mapper.shelterToShelterDto(allShelterEntities.get(0)))
                .build();
            // might throw ValidationException
            UUID id = animalService.create(createAnimalDto).getId();
            CreateFileDto createFileDto = CreateFileDto.builder()
                .name(TEST_FILE_NAME)
                .summary(TEST_FILE_SUMMARY)
                // might throw IOException
                .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
                .build();

            // might throw ValidationException, ConflictException, NotFoundException
            FileDto returnFile = animalService.setProfileImage(id, createFileDto);

            assertAll(
                () -> assertNotNull(returnFile),
                () -> assertEquals(returnFile.getName(), createFileDto.getName()),
                () -> assertEquals(returnFile.getSummary(), createFileDto.getSummary()),
                () -> assertEquals(returnFile.getContentMimeType(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())))
            );

        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void setProfileImageWhenAlreadySetThrowsConflictException() throws IOException {
        UUID id = allAnimalEntities.get(0).getId();
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();

        assertThrows(ConflictException.class, () -> animalService.setProfileImage(id, createFileDto));
    }

    @Test
    public void setProfileImageWithEmptyImageThrowsValidationException() {
        assertDoesNotThrow(() -> {
            CreateAnimalDto createAnimalDto = CreateAnimalDto.builder()
                .name(TEST_ANIMAL_NAME)
                .species(TEST_ANIMAL_SPECIES)
                .race(TEST_ANIMAL_RACE)
                .birthDate(TEST_ANIMAL_BIRTH_DATE)
                .height(TEST_ANIMAL_HEIGHT)
                .weight(TEST_ANIMAL_WEIGHT)
                .shelter(mapper.shelterToShelterDto(allShelterEntities.get(0)))
                .build();

            // might throw ValidationException, ConflictException
            UUID id = animalService.create(createAnimalDto).getId();
            CreateFileDto createFileDto = CreateFileDto.builder()
                .name(TEST_FILE_NAME)
                .summary(TEST_FILE_SUMMARY)
                // NO FILE INPUT
                .build();

            assertThrows(ValidationException.class, () -> animalService.setProfileImage(id, createFileDto));
        });
    }

    // function - UPDATEPROFILEIMAGE
    // POSITIVE TEST(S)
    @Test
    public void updateProfileImageSuccessfully() {
        UUID id = allAnimalEntities.get(0).getId();

        assertDoesNotThrow(() -> {
            CreateFileDto createFileDto = CreateFileDto.builder()
                .name(TEST_FILE_NAME)
                .summary(TEST_FILE_SUMMARY)
                // might throw IOException
                .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
                .build();

            // might throw ValidationException, NotFoundException, IOException
            FileDto returnFile = animalService.updateProfileImage(id, createFileDto);

            assertAll(
                () -> assertNotNull(returnFile),
                () -> assertEquals(returnFile.getName(), createFileDto.getName()),
                () -> assertEquals(returnFile.getSummary(), createFileDto.getSummary()),
                () -> assertEquals(returnFile.getContentMimeType(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())))
            );
        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void updateProfileImageForNonExistingAnimalThrowsNotFoundException() throws IOException {
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();
        assertThrows(NotFoundException.class, () -> animalService.updateProfileImage(UUID.randomUUID(), createFileDto));
    }

    // function - GETCONTENTS
    // POSITIVE TEST(S)
    @Test
    @Transactional
    public void getContentsSuccessfully() {

        assertDoesNotThrow(() -> {
            // might throw NotFoundException
            List<FileDto> returnContentFiles = animalService.getContents(allAnimalEntities.get(0).getId());

            assertAll(
                () -> assertEquals(returnContentFiles.size(), allAnimalEntities.get(0).getContent().size())
            );
        });
    }

    // function - GETCONTENT
    // POSITIVE TEST(S)
    @Test
    @Transactional
    public void getContentSuccessfully() {
        Animal animal = allAnimalEntities.get(0);
        File givenContentFile = animal.getContent().get(0);

        assertDoesNotThrow(() -> {
            // might throw NotFoundException
            FileDetailDto returnContentFile = animalService.getContent(animal.getId(), animal.getContent().get(0).getId());

            assertAll(
                () -> assertNotNull(returnContentFile),
                () -> assertEquals(returnContentFile.getName(), givenContentFile.getName()),
                () -> assertEquals(returnContentFile.getSummary(), givenContentFile.getSummary()),
                () -> assertEquals(returnContentFile.getContentLength(), givenContentFile.getContentLength()),
                () -> assertEquals(returnContentFile.getCreatedOn(), givenContentFile.getCreatedOn()),
                () -> assertEquals(returnContentFile.getContentMimeType(), givenContentFile.getContentMimeType())
            );
        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void getContentWithNonExistingContentIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> animalService.getContent(allAnimalEntities.get(0).getId(), UUID.randomUUID()));
    }

    // function - ADDCONTENT
    // POSITIVE TEST(S)
    @Test
    public void addContentSuccessfully() {
        assertDoesNotThrow(() -> {
            CreateFileDto createFileDto = CreateFileDto.builder()
                .name(TEST_FILE_NAME)
                .summary(TEST_FILE_SUMMARY)
                .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
                .build();

            // might throw NotFoundException, ValidationException
            FileDto returnContentFile = animalService.addContent(allAnimalEntities.get(1).getId(), createFileDto);

            assertAll(
                () -> assertNotNull(returnContentFile),
                () -> assertEquals(returnContentFile.getName(), TEST_FILE_NAME),
                () -> assertEquals(returnContentFile.getSummary(), TEST_FILE_SUMMARY),
                () -> assertEquals(returnContentFile.getContentMimeType(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())))
            );
        });
    }

    // NEGATIVE TEST(S)
    @Test
    public void addContentWithoutActualImageFileThrowsValidationException() {
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            //.file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            // EMPTY FILE
            .build();
        assertThrows(ValidationException.class, () -> animalService.addContent(allAnimalEntities.get(1).getId(), createFileDto));
    }

    // function - DELETE CONTENT
    // POSITIVE TEST(S)
    @Test
    @Transactional
    public void deleteContentSuccessfully() {
        Animal animal = allAnimalEntities.get(0);
        int initialContentListSize = animal.getContent().size();
        UUID fileIdToDelete = animal.getContent().get(0).getId();

        assertAll(
            () -> assertDoesNotThrow(() -> animalService.deleteContent(animal.getId(), fileIdToDelete)),
            () -> assertEquals(animalService.getContents(animal.getId()).size(), initialContentListSize - 1),
            () -> assertThrows(NotFoundException.class, () -> animalService.getContent(animal.getId(), fileIdToDelete))
        );
    }

    // NEGATIVE TEST(S)
    @Test
    @Transactional
    public void deleteNonExistingContentThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> animalService.deleteContent(allAnimalEntities.get(0).getId(), UUID.randomUUID()));
    }

}