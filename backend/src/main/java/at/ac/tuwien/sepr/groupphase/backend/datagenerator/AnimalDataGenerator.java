package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.UUID;

@Profile("generateData")
@Component
@RequiredArgsConstructor
public class AnimalDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int AMOUNT = 18;
    private static final String NAME = "Herbert";
    private static final Species SPECIES = Species.CAT;
    private static final String RACE = "Brown Cat";
    private static final LocalDate BIRTH_DATE = LocalDate.now();
    private static final double HEIGHT = 0.4;
    private static final double WEIGHT = 20;

    private static final Shelter SHELTER = Shelter.builder()
        .id(UUID.randomUUID())
        .name("TEST Gruft")
        .address("Teststrasse 1")
        .phoneNumber("+43 4241 403938")
        .email("die@gruft")
        .build();


    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    @PostConstruct
    private void generateAnimals() {
        LOGGER.debug("generating demo shelter ({})", SHELTER);
        Shelter shelter = shelterRepository.save(SHELTER);

        if (!animalRepository.findAll().isEmpty()) {
            LOGGER.debug("animals already generated");
        } else {
            LOGGER.debug("generating {} animal entries", AMOUNT);
            for (int i = 1; i <= AMOUNT; i++) {
                Animal animal = Animal.builder()
                    .name(NAME + i)
                    .species(SPECIES)
                    .race(RACE)
                    .birthDate(BIRTH_DATE)
                    .height(HEIGHT)
                    .weight(WEIGHT)
                    .shelter(shelter)
                    .build();
                LOGGER.debug("saving animal {}", animal);
                animalRepository.save(animal);
            }
        }
    }
}
