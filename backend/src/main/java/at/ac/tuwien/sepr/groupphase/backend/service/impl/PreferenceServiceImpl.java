package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Preference;
import at.ac.tuwien.sepr.groupphase.backend.entity.PreferenceId;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PreferenceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PreferenceRepository preferenceRepository;
    private final AnimalService animalService;
    private final UserRepository userRepository;

    @Override
    public void like(UUID animalId, UUID userId) throws NotFoundException {
        LOGGER.trace("Change Preferences, animal with id {} user with id{}", animalId, userId);
        AnimalDetailDto animal = animalService.findById(animalId);
        var user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("User with id " + userId + " does not exist.")
        );
        PreferenceId preferenceId = new PreferenceId(user, animal.getSpecies());
        if (preferenceRepository.findById(preferenceId).isPresent()) {
            Preference preference = preferenceRepository.findById(preferenceId).get();
            preference.setSpeciesFactor((long) (preference.getSpeciesFactor() * 1.01));
            preferenceRepository.save(preference);
        } else {
            Map<String, Integer> traitPoints = new HashMap<>();
            traitPoints.put("race", 0);
            traitPoints.put("height", 0);
            traitPoints.put("weight", 0);
            traitPoints.put("age", 0);

            preferenceRepository.save(Preference.builder()
                .user(user)
                .species(animal.getSpecies())
                .speciesFactor(1L)
                .traitPoints(traitPoints)
                .build());
        }

    }
}
