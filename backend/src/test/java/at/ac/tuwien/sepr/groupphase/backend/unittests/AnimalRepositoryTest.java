package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.AnimalEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class AnimalRepositoryTest implements TestData {

    @Autowired
    private AnimalRepository animalRepository;


    // POSITIVE TESTS

    @Test
    public void createNewAnimalWithSuccess(){
        Animal animal = Animal.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .build();

        Animal returnAnimal = animalRepository.getById(animalRepository.save(animal).getId());


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
