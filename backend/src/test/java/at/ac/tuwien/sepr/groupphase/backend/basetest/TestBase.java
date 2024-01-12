package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.datagenerator.AllDataGenerator;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ComponentScan(basePackages = {"at.ac.tuwien.sepr.groupphase.backend"})
@ActiveProfiles("test")
public abstract class TestBase {

    protected List<Shelter> allShelterEntities;
    protected List<Animal> allAnimalEntities;
    protected List<File> allFileEntities;
    protected List<Subscription> allSubscriptionEntities;
    protected List<ApplicationUser> allUserEntities;

    @Autowired
    protected AllDataGenerator dataGenerator;
    protected String BASE_URI = "/api/v1";
    // ### ANIMAL TestData
    protected String TEST_ANIMAL_NAME = "Herbert";

    // ### BASIC TestData
    protected Species TEST_ANIMAL_SPECIES = Species.CAT;
    protected String TEST_ANIMAL_RACE = "Angoran";
    protected LocalDate TEST_ANIMAL_BIRTH_DATE =
        LocalDate.of(2019, 11, 13);
    protected double TEST_ANIMAL_HEIGHT = 0.3;
    protected double TEST_ANIMAL_WEIGHT = 3;
    protected String ANIMALS_BASE_URI = BASE_URI + "/animals";
    // ### SHELTER TestData
    protected String TEST_SHELTER_NAME = "TEST SHELTER";

    protected String TEST_SHELTER_DONATION_ID = "Q3UV965BMX2FW";
    protected String TEST_SHELTER_ADDRESS = "TEST STREET 33, 0000 TEST";
    protected String TEST_SHELTER_PHONE_NUMBER = "+43 4241 403938";
    protected String TEST_SHELTER_EMAIL = "test@testing.com";
    protected String TEST_FILE_NAME = "TEST FILE";
    protected String TEST_FILE_SUMMARY = "TEST SUMMARY";


    // ### FILE TestData
    protected java.io.File TEST_FILE_FILE = Arrays.stream(new java.io.File("src/test/resources/demo data").listFiles()).findFirst().orElseThrow(() -> new IllegalArgumentException("the given directory for demo files is not accessible!"));

    @BeforeEach
    public void setupDb() throws SQLException {
        dataGenerator.generateData();
        allShelterEntities = dataGenerator.getAllShelterEntities();
        allAnimalEntities = dataGenerator.getAllAnimalEntities();
        allFileEntities = dataGenerator.getAllFileEntities();
        allUserEntities = dataGenerator.getAllApplicationUserEntities();
    }

    @AfterEach
    public void tearDownDb() throws SQLException {
        dataGenerator.clearData();
        allSubscriptionEntities = Collections.emptyList();
        allUserEntities = Collections.emptyList();
        allShelterEntities = Collections.emptyList();
        allAnimalEntities = Collections.emptyList();
        allFileEntities = Collections.emptyList();
    }


}
