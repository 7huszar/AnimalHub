package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalRankingDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Preference;
import at.ac.tuwien.sepr.groupphase.backend.entity.PreferenceId;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.LikedDislikedAnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PreferenceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    private final AnimalRepository animalRepository;
    private final AnimalShelterMapper animalShelterMapper;

    @Override
    @Transactional
    public void like(UUID animalId, UUID userId) throws NotFoundException {
        LOGGER.trace("like animal with id {} user with id{}", animalId, userId);
        var user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("User with id " + userId + " does not exist.")
        );
        var animal = animalRepository.findById(animalId).orElseThrow(
            () -> new NotFoundException("Animal with id " + animalId + " does not exist.")
        );
        likedDislikedAnimalRepository.save(LikedDislikedAnimal.builder()
            .user(user)
            .animal(animal)
            .liked(true)
            .build());


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
        weight /= getLikedSpecies(userId, animal.getSpecies()).size();
        traitPoints.put("weight", weight);
        height /= getLikedSpecies(userId, animal.getSpecies()).size();
        traitPoints.put("height", height);
        age /= getLikedSpecies(userId, animal.getSpecies()).size();
        traitPoints.put("age", age);

        if (!traitPoints.containsKey("gender_" + animal.getGender())) {
            traitPoints.put("gender_" + animal.getGender(), 1.0);
        }
        traitPoints.put("gender_" + animal.getGender(), traitPoints.get("gender_" + animal.getGender()) * 1.01);
    }

    @Override
    @Transactional
    public void dislike(UUID animalId, UUID userId) throws NotFoundException {
        LOGGER.trace("Change Preferences, animal with id {} user with id{}", animalId, userId);
        AnimalDetailDto animal = animalService.findById(animalId);
        var user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("User with id " + userId + " does not exist.")
        );
        var animal2 = animalRepository.findById(animalId).orElseThrow(
            () -> new NotFoundException("Animal with id " + animalId + " does not exist.")
        );
        likedDislikedAnimalRepository.save(LikedDislikedAnimal.builder()
            .user(user)
            .animal(animal2)
            .liked(false)
            .build());


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
        preference.setSpeciesFactor((preference.getSpeciesFactor() * 0.99));
        preferenceRepository.save(preference);
        Map<String, Double> traitPoints = preference.getTraitPointMap();
        if (!traitPoints.containsKey("gender_" + animal.getGender())) {
            traitPoints.put("gender_" + animal.getGender(), 1.0);
        }
        traitPoints.put("gender_" + animal.getGender(), traitPoints.get("gender_" + animal.getGender()) * 0.99);
    }

    @Override
    @Transactional
    public List<AnimalRankingDto> getRecommendedAnimals(UUID userId) throws NotFoundException {
        List<AnimalDetailDto> animals = animalService.findAll();
        List<AnimalDetailDto> likedAnimals = getLikedAnimals(userId);
        animals.removeIf(animal -> likedAnimals.stream().anyMatch(animal1 -> animal1.getId().equals(animal.getId())));
        List<AnimalDetailDto> dislikedAnimals = getLikedAnimals(userId);
        animals.removeIf(animal -> dislikedAnimals.stream().anyMatch(animal1 -> animal1.getId().equals(animal.getId())));
        List<AnimalRankingDto> animalRanking = new ArrayList<>();
        for (AnimalDetailDto animal : animals) {
            double score = 0;
            if (userRepository.findById(userId).isPresent()) {
                if (preferenceRepository.findById(new PreferenceId(userRepository.findById(userId).get(), animal.getSpecies())).isPresent()) {
                    Preference preference = preferenceRepository.findById(new PreferenceId(userRepository.findById(userId).get(), animal.getSpecies())).get();
                    if (preference.getTraitPointMap().containsKey("gender_" + animal.getGender().toString().toLowerCase())) {
                        score += preference.getTraitPointMap().get("gender_" + animal.getGender().toString().toLowerCase());
                    }
                    if (preference.getTraitPointMap().containsKey("weight")) {
                        if (preference.getTraitPointMap().get("weight") / animal.getWeight() > 1) {
                            score += 1 - preference.getTraitPointMap().get("weight") / animal.getWeight() * 0.1;
                        } else {
                            score += preference.getTraitPointMap().get("weight") / animal.getWeight() * 0.1 + 0.9;
                        }
                    }
                    if (preference.getTraitPointMap().containsKey("height")) {
                        if (preference.getTraitPointMap().get("height") / animal.getHeight() > 1) {
                            score += 1 - preference.getTraitPointMap().get("height") / animal.getHeight() * 0.1;
                        } else {
                            score += preference.getTraitPointMap().get("height") / animal.getHeight() * 0.1 + 0.9;
                        }
                    }
                    if (preference.getTraitPointMap().containsKey("age")) {
                        int age = Period.between(animal.getBirthDate(), LocalDate.now()).getYears();
                        if (age != 0) {
                            if (preference.getTraitPointMap().get("age") / age > 1) {
                                score += 1 - preference.getTraitPointMap().get("age") / age * 0.1;
                            } else {
                                score += preference.getTraitPointMap().get("age") / age * 0.1 + 0.9;
                            }
                        }

                    }
                    score *= preference.getSpeciesFactor();
                }
            }

            animalRanking.add(new AnimalRankingDto(animal, score));
        }
        animalRanking.sort(new Comparator<AnimalRankingDto>() {
            @Override
            public int compare(AnimalRankingDto o1, AnimalRankingDto o2) {
                return Double.compare(o2.getScore(), o1.getScore());
            }
        });

        return animalRanking;
    }

    @Override
    public List<AnimalDetailDto> getDislikedAnimals(UUID userId) {
        LOGGER.trace("Find all disliked Animals");

        return likedDislikedAnimalRepository.findByUserIdAndLiked(userId, false).stream()
            .map(this::mapToAnimalDetailDto).toList();
    }

    @Override
    public List<AnimalDetailDto> getLikedAnimals(UUID userId) {
        LOGGER.trace("Find all liked Animals");

        return likedDislikedAnimalRepository.findByUserIdAndLiked(userId, true).stream()
            .map(this::mapToAnimalDetailDto).toList();
    }

    @Override
    public List<AnimalDetailDto> getLikedSpecies(UUID userId, Species species) throws NotFoundException {
        LOGGER.trace("Find all liked Animals with species {}", species);

        List<AnimalDetailDto> animals = new ArrayList<>(likedDislikedAnimalRepository.findByUserIdAndLiked(userId, true).stream()
                .map(this::mapToAnimalDetailDto).toList());
        animals.removeIf(animal -> animal.getSpecies() != species);
        return animals;
    }

    @Override
    public void removeLikedAnimal(UUID userId, UUID animalId) throws NotFoundException {
        LOGGER.trace("delete({}{})", userId, animalId);

        var animal = likedDislikedAnimalRepository.findByUserIdAndAnimalId(userId, animalId);

        likedDislikedAnimalRepository.delete(animal);
    }

    private AnimalDetailDto mapToAnimalDetailDto(LikedDislikedAnimal animal) {
        return animalShelterMapper.animalToAnimalDetailDto(animal.getAnimal());
    }




}
