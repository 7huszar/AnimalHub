package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@RequiredArgsConstructor
public class ShelterDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final int amount = 3;
    private final String[] shelterNames = {"Die Gruft", "Herzheim", "Faunafest"};
    private final String[] shelterAddresses = {"Gruftgasse 1, 1100 Wien", "Herzplatz 33, 7077 Lungenstadt", "Palmenhof 13/12, 2300 Dschungeltown"};
    private final String[] shelterPhoneNumbers = {"+43 4241 403938", "+37 3635 343332", "+31 3029 282726"};
    private final String[] shelterEmails = {"die@gruft.at", "innereien@herzheim.com", "nichtpflanzlich@dschungel.org"};

    private final String[] shelterProfilePictureNames = {"shelter demo 1", "shelter demo 2", "shelter demo 3"};

    private final ShelterRepository shelterRepository;
    private final FileRepository fileRepository;


    public void generateShelters() {
        if (!shelterRepository.findAll().isEmpty()) {
            LOGGER.debug("shelters already generated");
        } else {
            LOGGER.debug("generating demo shelters");
            for (int i = 0; i < amount; i++) {
                Shelter shelter = Shelter.builder()
                    .name(shelterNames[i])
                    .address(shelterAddresses[i])
                    .phoneNumber(shelterPhoneNumbers[i])
                    .email(shelterEmails[i])
                    .profilePicture(fileRepository.findByName(shelterProfilePictureNames[i])[0])
                    .build();
                LOGGER.debug("saving shelter {}", shelter);
                shelterRepository.save(shelter);
            }
        }
    }
}
