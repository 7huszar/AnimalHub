package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface TestData {

    // ### BASIC TestData

    String BASE_URI = "/api/v1";

    // ### ANIMAL TestData
    UUID TEST_ANIMAL_ID = UUID.randomUUID();
    String TEST_ANIMAL_NAME = "Herbert";
    Species TEST_ANIMAL_SPECIES = Species.CAT;
    String TEST_ANIMAL_RACE = "Angoran";
    LocalDate TEST_ANIMAL_BIRTH_DATE =
        LocalDate.of(2019, 11, 13);
    double TEST_ANIMAL_HEIGHT = 0.3;
    double TEST_ANIMAL_WEIGHT = 3;
    String ANIMALS_BASE_URI = BASE_URI + "/animals";

    // ### SHELTER TestData

    UUID SHELTER_ID = UUID.randomUUID();

    String TEST_SHELTER_NAME = "Die Gruft";
    String TEST_SHELTER_ADDRESS = "Gruftgasse 666, 1150 Wien";
    String TEST_SHELTER_PHONE_NUMBER = "+43 4241 403938";
    String TEST_SHELTER_EMAIL = "die@gruft.at";
    List<Animal> TEST_SHELTER_ANIMALS = Collections.singletonList(Animal.builder()
        .id(TEST_ANIMAL_ID)
        .name(TEST_ANIMAL_NAME)
        .species(TEST_ANIMAL_SPECIES)
        .race(TEST_ANIMAL_RACE)
        .birthDate(TEST_ANIMAL_BIRTH_DATE)
        .height(TEST_ANIMAL_HEIGHT)
        .weight(TEST_ANIMAL_WEIGHT)
        .build());


    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

}
