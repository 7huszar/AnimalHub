package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@SpringBootTest
@ActiveProfiles("test")
public class AnimalServiceTest implements TestData {

    @Autowired
    private AnimalService animalService;


    // POSITIVE TESTS

    @Test
    public void createNewAnimalWithSuccess() throws ValidationException {
        CreateAnimalDto animalDto = CreateAnimalDto.builder()
            .name(TEST_ANIMAL_NAME)
            .species(TEST_ANIMAL_SPECIES)
            .race(TEST_ANIMAL_RACE)
            .birthDate(TEST_ANIMAL_BIRTH_DATE)
            .height(TEST_ANIMAL_HEIGHT)
            .weight(TEST_ANIMAL_WEIGHT)
            .build();

        AnimalDetailDto returnAnimal = animalService.findById(animalService.create(animalDto).getId());


        assertAll(
            () -> assertEquals(1, animalService.findAll().size()),
            () -> assertEquals(animalDto.getName(), returnAnimal.getName()),
            () -> assertEquals(animalDto.getSpecies(), returnAnimal.getSpecies()),
            () -> assertEquals(animalDto.getRace(), returnAnimal.getRace()),
            () -> assertEquals(animalDto.getBirthDate(), returnAnimal.getBirthDate()),
            () -> assertEquals(animalDto.getHeight(), returnAnimal.getHeight()),
            () -> assertEquals(animalDto.getWeight(), returnAnimal.getWeight())
        );
    }

}
