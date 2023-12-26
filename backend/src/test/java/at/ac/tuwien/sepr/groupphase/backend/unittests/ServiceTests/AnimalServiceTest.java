package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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


    @Test
    public void findAllSuccessfully() throws ValidationException, ConflictException {
        List<AnimalDetailDto> result = animalService.findAll();

        assertAll(
            () -> assertEquals(result.size(), allAnimalEntities.size())
        );
    }


    @Test
    public void createNewAnimalWithSuccess() throws ValidationException, ConflictException, NotFoundException {

        CreateAnimalDto createAnimalDto = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(mapper.shelterToShelterDto(allShelterEntities.get(0)))
            .build();

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
    }

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
    public void createNewAnimalUnsuccessfullyInvalidNameAndWeight() throws ValidationException {

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

    @Test
    public void deleteAnimalSuccessfully() throws ValidationException, ConflictException, NotFoundException {

        UUID animalId = allAnimalEntities.get(0).getId();

        animalService.delete(animalId);

        assertAll(
            () -> assertEquals(allAnimalEntities.size() - 1, animalService.findAll().size()),
            () -> assertThrows(NotFoundException.class, () -> animalService.findById(animalId))
        );
    }
}