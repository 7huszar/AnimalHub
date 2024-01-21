package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalRankingDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Preference;
import at.ac.tuwien.sepr.groupphase.backend.entity.PreferenceId;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.LikedDislikedAnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PreferenceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.PreferenceService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PreferenceRepository preferenceRepository;
    private final AnimalService animalService;
    private final UserRepository userRepository;
    private final LikedDislikedAnimalRepository likedDislikedAnimalRepository;

    @Override
    @Transactional
    public void like(UUID animalId, UUID userId) throws NotFoundException {
        LOGGER.trace("Change Preferences, animal with id {} user with id{}", animalId, userId);
        AnimalDetailDto animal = animalService.findById(animalId);
        var user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("User with id " + userId + " does not exist.")
        );
        //TODO: implement here @Tobias
        PreferenceId preferenceId = new PreferenceId(user, animal.getSpecies());
        if (preferenceRepository.findById(preferenceId).isEmpty()) {
            preferenceRepository.save(Preference.builder()
                .user(user)
                .species(animal.getSpecies())
                .speciesFactor(1)
                .traitPointMap(new HashMap<>())
                .build());
        }
        Preference preference = preferenceRepository.findById(preferenceId).get();
        preference.setSpeciesFactor((preference.getSpeciesFactor() * 1.01));
        preferenceRepository.save(preference);
        Map<String, Double> traitPoints = preference.getTraitPointMap();
        double weight = 0;
        double height = 0;
        double age = 0;
        if (!traitPoints.containsKey("weight")) {
            traitPoints.put("weight", 1.0);
        }
        if (!traitPoints.containsKey("height")) {
            traitPoints.put("height", 1.0);
        }
        if (!traitPoints.containsKey("age")) {
            traitPoints.put("age", 1.0);
        }
        for (AnimalDetailDto animalDetailDto : getLikedAnimals(userId)) {
            weight += animalDetailDto.getWeight();
            height += animalDetailDto.getHeight();
            age += Period.between(animalDetailDto.getBirthDate(), LocalDate.now()).getYears();
        }
        weight /= getLikedAnimals(userId).size();
        traitPoints.put("weight", weight);
        height /= getLikedAnimals(userId).size();
        traitPoints.put("height", height);
        age /= getLikedAnimals(userId).size();
        traitPoints.put("age", age);
        if (!traitPoints.containsKey("gender_" + animal.getGender())) {
            traitPoints.put("gender_" + animal.getGender(), 1.0);
        }
        traitPoints.put("gender_" + animal.getGender(), traitPoints.get("gender_" + animal.getGender()) * 1.01);


    }

    @Override
    public List<AnimalDetailDto> getRecommendedAnimals(UUID userId) throws NotFoundException {
        List<AnimalDetailDto> animals = animalService.findAll();
        animals.removeAll(getLikedAnimals(userId));
        animals.removeAll(getDislikedAnimals(userId));
        List<AnimalRankingDto> animalRanking;
        for (AnimalDetailDto animal : animals) {
            double score = 0;
            animalRanking.add(new AnimalRankingDto(animal, score);
        }
        return null;
    }

    @Override
    public List<AnimalDetailDto> getDislikedAnimals(UUID userId) throws NotFoundException {
        return null;
    }

    @Override
    public List<AnimalDetailDto> getLikedAnimals(UUID userId) throws NotFoundException {
        //TODO: implement @Tobias
        return null;
    }
}
