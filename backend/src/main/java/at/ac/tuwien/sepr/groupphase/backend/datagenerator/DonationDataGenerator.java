package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.enums.DonationType;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DonationDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final DonationRepository donationRepository;

    public void generateDonations() {

        LOGGER.info("generateDonations()");

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, String>> donationConfig;

        try {
            donationConfig = objectMapper.readValue(new File("src/test/resources/demo data/DonationConfig.json"),
                new TypeReference<List<Map<String, String>>>() {});

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Animal> animals = animalRepository.findAll();
        int i = 0;
        ApplicationUser user = userRepository.findByEmail("donator@animalhub");

        for (Map<String, String> donation : donationConfig) {
            Donation don = Donation.builder()
                .id(donation.get("id"))
                .stamp(LocalDateTime.parse(donation.get("stamp"), formatter))
                .amount(new BigDecimal(donation.get("amount")))
                .receiver(animals.get(i).getId())
                .shelter(animals.get(i).getShelter())
                .payed(true)
                .type(DonationType.ONE_TIME)
                .user(user)
                .build();

            donationRepository.save(don);

            i = (i + 1) % animals.size();
        }
    }
}
