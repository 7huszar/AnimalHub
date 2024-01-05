package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.List;

@Profile({"generateData", "test"})
@Component
@RequiredArgsConstructor
public class AllDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private final FileRepository fileRepository;
    @Autowired
    private final ShelterRepository shelterRepository;
    @Autowired
    private final AnimalRepository animalRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private FileDataGenerator fileDataGenerator;
    @Autowired
    private ShelterDataGenerator shelterDataGenerator;
    @Autowired
    private AnimalDataGenerator animalDataGenerator;
    @Autowired
    private UserDataGenerator userDataGenerator;

    @PostConstruct
    public void generateData() {
        LOGGER.debug("generating data... (Files -> Shelters -> Animals -> Users)");
        fileDataGenerator.generateFiles();
        shelterDataGenerator.generateShelters();
        animalDataGenerator.generateAnimals();
        userDataGenerator.generateUsers();
    }

    public void clearData() {
        userRepository.deleteAll();
        animalRepository.deleteAll();
        shelterRepository.deleteAll();
        fileRepository.deleteAll();
        for (java.io.File f :
            new java.io.File("files").listFiles()) {
            f.delete();
        }
    }

    public List<Shelter> getAllShelterEntities() {
        return shelterRepository.findAll();
    }

    public List<Animal> getAllAnimalEntities() {
        return animalRepository.findAll();
    }

    public List<File> getAllFileEntities() {
        return fileRepository.findAll();
    }

}
