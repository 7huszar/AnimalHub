package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_ANIMAL_NAME = "Herbert";
    Species TEST_ANIMAL_SPECIES = Species.CAT;
    String TEST_ANIMAL_RACE = "Angoran";
    LocalDate TEST_ANIMAL_BIRTH_DATE =
        LocalDate.of(2019, 11, 13);

    double TEST_ANIMAL_HEIGHT = 0.3;

    double TEST_ANIMAL_WEIGHT = 3;

    String BASE_URI = "/api/v1";
    String ANIMALS_BASE_URI = BASE_URI + "/animals";

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
