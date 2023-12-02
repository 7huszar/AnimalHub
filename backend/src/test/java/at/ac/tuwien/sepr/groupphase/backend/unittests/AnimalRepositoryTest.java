package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class AnimalRepositoryTest implements TestData {

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private ShelterRepository shelterRepository;


    // POSITIVE TESTS

    @Test
    public void createNewAnimalWithSuccess() {

        Shelter shelter = Shelter.builder()
            .id(TEST_SHELTER_ID)
            .name(TEST_SHELTER_NAME)
            .address(TEST_SHELTER_ADDRESS)
            .phoneNumber(TEST_SHELTER_PHONE_NUMBER)
            .email(TEST_SHELTER_EMAIL)
            .build();

        assertDoesNotThrow(() -> {
            shelterRepository.save(shelter);
        });


        Animal animal = Animal.builder()
            .id(TEST_ANIMAL_ID)
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .shelter(shelter)
            .build();

        Animal returnAnimal = animalRepository.save(animal);


        assertAll(
            () -> assertEquals(1, animalRepository.findAll().size()),
            () -> assertEquals(animal.getName(), returnAnimal.getName()),
            () -> assertEquals(animal.getSpecies(), returnAnimal.getSpecies()),
            () -> assertEquals(animal.getRace(), returnAnimal.getRace()),
            () -> assertEquals(animal.getBirthDate(), returnAnimal.getBirthDate()),
            () -> assertEquals(animal.getHeight(), returnAnimal.getHeight()),
            () -> assertEquals(animal.getWeight(), returnAnimal.getWeight())
        );
    }

}
