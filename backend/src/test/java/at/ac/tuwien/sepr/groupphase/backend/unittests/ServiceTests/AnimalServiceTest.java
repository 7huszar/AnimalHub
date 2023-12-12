package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animalDtos.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animalDtos.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AnimalServiceTest implements TestData {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    private final AnimalService animalService;
    private final ShelterService shelterService;

    private final AnimalShelterMapper mapper;

    @BeforeEach
    public void beforeEach() {
        shelterRepository.deleteAll();
        animalRepository.deleteAll();   // at this point there shouldnt be any animals in the repo (just to be sure)
    }

    @Test
    public void findAllSuccessfully() throws ValidationException, ConflictException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDetailDto = shelterService.create(createShelterDto);

        CreateAnimalDto animalDto1 = CreateAnimalDto.builder()
            .name("Test1")
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(12)
            .weight(12)
            .shelter(shelterDetailDto)
            .build();

        //create the animal
        AnimalDetailDto animalDetailDto = animalService.create(animalDto1);

        CreateAnimalDto animalDto2 = CreateAnimalDto.builder()
            .name("Test1")
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(12)
            .weight(12)
            .shelter(shelterDetailDto)
            .build();

        //create the animal
        AnimalDetailDto animalDetailDto1 = animalService.create(animalDto2);



        assertAll(

            () -> assertEquals(animalDto1.getName(), animalDetailDto.getName()),
            () -> assertEquals(animalDto1.getSpecies(), animalDetailDto.getSpecies()),
            () -> assertEquals(animalDto1.getRace(), animalDetailDto.getRace()),
            () -> assertEquals(animalDto1.getBirthDate(), animalDetailDto.getBirthDate()),
            () -> assertEquals(animalDto1.getHeight(), animalDetailDto.getHeight()),
            () -> assertEquals(animalDto1.getWeight(), animalDetailDto.getWeight()),

            () -> assertEquals(animalDto2.getName(), animalDetailDto1.getName()),
            () -> assertEquals(animalDto2.getSpecies(), animalDetailDto1.getSpecies()),
            () -> assertEquals(animalDto2.getRace(), animalDetailDto1.getRace()),
            () -> assertEquals(animalDto2.getBirthDate(), animalDetailDto1.getBirthDate()),
            () -> assertEquals(animalDto2.getHeight(), animalDetailDto1.getHeight()),
            () -> assertEquals(animalDto2.getWeight(), animalDetailDto1.getWeight())
        );
    }


    @Test
    public void createNewAnimalWithSuccess() throws ValidationException, ConflictException {

        CreateShelterDto createShelterDto = CreateShelterDto.builder()
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .createdOn(Instant.now())
            .build();

        ShelterDto shelterDto = shelterService.create(createShelterDto);

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
            () -> assertEquals(createShelterDto.getName(), shelterDto.getName()),
            () -> assertEquals(createShelterDto.getAddress(), shelterDto.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), shelterDto.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), shelterDto.getEmail()),
            () -> assertEquals(createShelterDto.getCreatedOn(), shelterDto.getCreatedOn())
        );


        CreateAnimalDto animalDto = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(shelterDto)
            .build();

        AnimalDetailDto animalDetailDto = animalService.create(animalDto);



        assertAll(
            () -> assertEquals(animalDto.getName(), animalDetailDto.getName()),
            () -> assertEquals(animalDto.getSpecies(), animalDetailDto.getSpecies()),
            () -> assertEquals(animalDto.getRace(), animalDetailDto.getRace()),
            () -> assertEquals(animalDto.getBirthDate(), animalDetailDto.getBirthDate()),
            () -> assertEquals(animalDto.getHeight(), animalDetailDto.getHeight()),
            () -> assertEquals(animalDto.getWeight(), animalDetailDto.getWeight())
        );

        ShelterDetailDto foundAnimalsInShelter = shelterService.findById(shelterDto.getId());

        assertAll(
            () -> assertEquals(1, shelterService.findAll().size()),
            () -> assertEquals(createShelterDto.getAddress(), foundAnimalsInShelter.getAddress()),
            () -> assertEquals(createShelterDto.getPhoneNumber(), foundAnimalsInShelter.getPhoneNumber()),
            () -> assertEquals(createShelterDto.getEmail(), foundAnimalsInShelter.getEmail()),
            () -> assertNotNull(foundAnimalsInShelter.getAnimals())
            // () -> assertEquals(createShelterDto.getCreatedOn(), foundAnimalsInShelter.getCreatedOn())
        );
    }

    @Test
    public void createNewAnimalUnsuccessfullyNotExistingShelter() {

        CreateAnimalDto animal = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(mapper.shelterToShelterDto(TEST_ANIMAL_SHELTER)) // Using a non-existent shelter
            .build();

        assertEquals(0, shelterService.findAll().size());

        assertThrows(ConflictException.class, () -> animalService.create(animal));
    }

    @Test
    public void createNewAnimalUnsuccessfullyInvalidNameAndWeight() throws ValidationException {

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


        CreateAnimalDto animalDto = CreateAnimalDto.builder()
            .name("")
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(0)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(shelterDetailDto)
            .build();

        assertThrows(ValidationException.class, () -> animalService.create(animalDto));
    }

    @Test
    public void deleteShelterAndAnimalsInItSuccessfully() throws ValidationException, ConflictException {

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


        CreateAnimalDto animalDto = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(shelterDetailDto)
            .build();

        AnimalDetailDto animalDetailDto = animalService.create(animalDto);


        shelterService.delete(shelterDetailDto.getId());

        assertAll(
            () -> assertEquals(0, shelterService.findAll().size()),
            () -> assertEquals(0, animalService.findAll().size())
        );
    }
}