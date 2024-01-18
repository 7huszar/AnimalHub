package at.ac.tuwien.sepr.groupphase.backend.unittests.RepositoryTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimalRepositoryTest extends TestBase {

    @Autowired
    private AnimalRepository animalRepository;

    // function - FIND ALL

    @Test
    public void findAllSuccessfully() {
        List<Animal> result = animalRepository.findAll();

        assertAll(
            () -> assertEquals(result.size(), allAnimalEntities.size())
        );
    }


    // function - CREATE
    // POSITIVE TEST(S)
    @Transactional
    @Test
    public void createNewAnimalSuccessfully() {
        Animal animal = Animal.builder()
            .name(TEST_ANIMAL_NAME)
            .gender(TEST_ANIMAL_GENDER)
            .englishDescription(TEST_ANIMAL_ENGLISH_DESCRIPTION)
            .germanDescription(TEST_ANIMAL_GERMAN_DESCRIPTION)
            .species(TEST_ANIMAL_SPECIES)
            .englishRace(TEST_ANIMAL_ENGLISH_RACE)
            .germanRace(TEST_ANIMAL_GERMAN_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(allShelterEntities.get(0))
            .build();

        Animal returnAnimal = animalRepository.save(animal);

        assertAll(
            () -> assertEquals(allAnimalEntities.size() + 1, animalRepository.findAll().size()),
            () -> assertEquals(animal.getName(), returnAnimal.getName()),
            () -> assertEquals(animal.getGender(), returnAnimal.getGender()),
            () -> assertEquals(animal.getEnglishDescription(), returnAnimal.getEnglishDescription()),
            () -> assertEquals(animal.getGermanDescription(), returnAnimal.getGermanDescription()),
            () -> assertEquals(animal.getSpecies(), returnAnimal.getSpecies()),
            () -> assertEquals(animal.getEnglishRace(), returnAnimal.getEnglishRace()),
            () -> assertEquals(animal.getGermanRace(), returnAnimal.getGermanRace()),
            () -> assertEquals(animal.getBirthDate(), returnAnimal.getBirthDate()),
            () -> assertEquals(animal.getHeight(), returnAnimal.getHeight()),
            () -> assertEquals(animal.getWeight(), returnAnimal.getWeight()),
            () -> assertEquals(animal.getShelter(), allShelterEntities.get(0))
        );
    }

    // NEGATIVE TEST(S)
    @Transactional
    @Test
    public void createNewEmptyAnimalUnsuccessfully() {
        Animal animal = null;

        assertAll(
            () -> assertThrows(InvalidDataAccessApiUsageException.class, () -> animalRepository.save(animal)),
            () -> assertEquals(animalRepository.findAll().size(), allAnimalEntities.size())
        );
    }

    // function - DELETE
    // POSITIVE TEST(S)

    @Transactional
    @Test
    public void deleteAnimalSuccessfully() {
        Animal deleteAnimal = allAnimalEntities.get(0);
        animalRepository.delete(deleteAnimal);

        assertAll(
            () -> assertEquals(animalRepository.findAll().size(), allAnimalEntities.size() - 1),
            () -> assertFalse(animalRepository.existsById(deleteAnimal.getId()))
        );

    }

    // NEGATIVE TEST(S)
    @Transactional
    @Test
    public void deleteNonExistingAnimalUnsuccessfully() {
        Animal nonExistingAnimal = Animal.builder()
            .id(UUID.randomUUID())
            .name(TEST_ANIMAL_NAME)
            .gender(TEST_ANIMAL_GENDER)
            .englishDescription(TEST_ANIMAL_ENGLISH_DESCRIPTION)
            .germanDescription(TEST_ANIMAL_GERMAN_DESCRIPTION)
            .species(TEST_ANIMAL_SPECIES)
            .englishRace(TEST_ANIMAL_ENGLISH_RACE)
            .germanRace(TEST_ANIMAL_GERMAN_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(allShelterEntities.get(0))
            .build();

        animalRepository.delete(nonExistingAnimal);

        assertEquals(animalRepository.findAll().size(), allAnimalEntities.size());

    }


}
