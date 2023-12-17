package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@RequiredArgsConstructor
public class AllDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileDataGenerator fileDataGenerator;
    private final ShelterDataGenerator shelterDataGenerator;
    private final AnimalDataGenerator animalDataGenerator;
    private final UserDataGenerator userDataGenerator;



    @PostConstruct
    private void generateData() {
        LOGGER.debug("generating data... (Files, Shelters, Animals & Users)");
        fileDataGenerator.generateFiles();
        shelterDataGenerator.generateShelters();
        animalDataGenerator.generateAnimals();
        userDataGenerator.generateUsers();
    }
}
