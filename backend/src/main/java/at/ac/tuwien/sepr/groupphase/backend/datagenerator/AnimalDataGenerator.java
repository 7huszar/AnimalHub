package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.Gender;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnimalDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int amount = 22;

    private final AnimalRepository animalRepository;
    private final FileRepository fileRepository;
    private final ShelterRepository shelterRepository;


    public void generateAnimals() {
        if (!animalRepository.findAll().isEmpty()) {
            LOGGER.debug("animals already generated");
        } else {
            LOGGER.debug("generating {} animal entries", amount);
            Map<String, String> animalConfig = new HashMap<String, String>();
            try {
                animalConfig = new ObjectMapper().readValue(new File("src/test/resources/demo data/AnimalConfig.json"), HashMap.class);

            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

            List<Shelter> allShelters = shelterRepository.findAll();
            int shelterIndex = 0;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int animalIndex = 1; animalIndex <= amount; animalIndex++) {

                // ADD DEMO CONTENT TO ANIMALS
                List<at.ac.tuwien.sepr.groupphase.backend.entity.File> allAnimalContent = new ArrayList<>(Collections.emptyList());

                if (!animalConfig.get("CONTENT " + animalIndex).isEmpty()) {
                    List<String> allContentFileNames = List.of(animalConfig.get("CONTENT " + animalIndex).split(";"));
                    for (String contentFileName :
                        allContentFileNames) {
                        allAnimalContent.add(fileRepository.findByName(contentFileName)[0]);
                    }
                }

                // CREATE NEW ANIMAL ENTITY
                Animal animal = Animal.builder()
                    .name(animalConfig.get("NAME " + animalIndex))
                    .gender((Gender.valueOf(animalConfig.get("GENDER " + animalIndex))))
                    .englishDescription(animalConfig.get("ENGLISH DESCRIPTION " + animalIndex))
                    .germanDescription(animalConfig.get("GERMAN DESCRIPTION " + animalIndex))
                    .profilePicture(fileRepository.findByName(animalConfig.get("PROFILE_PICTURE_NAME " + animalIndex))[0])
                    .species((Species.valueOf(animalConfig.get("SPECIES " + animalIndex))))
                    .englishRace(animalConfig.get("ENGLISH RACE " + animalIndex))
                    .germanRace(animalConfig.get("GERMAN RACE " + animalIndex))
                    .birthDate(LocalDate.parse(animalConfig.get("BIRTH_DATE " + animalIndex), dateFormatter))
                    .height(Double.parseDouble(animalConfig.get("HEIGHT " + animalIndex)))
                    .weight(Double.parseDouble(animalConfig.get("WEIGHT " + animalIndex)))
                    .shelter(allShelters.get(shelterIndex))
                    .content(allAnimalContent)
                    .build();
                LOGGER.debug("saving animal {}", animal);
                // SAVE NEW ANIMAL ENTITY
                animalRepository.save(animal);

                // DISTRIBUTE ANIMALS TO ALL DEMO SHELTERS
                ++shelterIndex;
                if (shelterIndex >= allShelters.size()) {
                    shelterIndex = 0;
                }

            }
        }
    }
}
