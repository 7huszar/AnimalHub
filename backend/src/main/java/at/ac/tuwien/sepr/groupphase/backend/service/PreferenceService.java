package at.ac.tuwien.sepr.groupphase.backend.service;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalRankingDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface PreferenceService {


    void like(UUID animalId, UUID userId) throws NotFoundException;

    void dislike(UUID animalId, UUID userId) throws NotFoundException;

    List<AnimalRankingDto> getRecommendedAnimals(UUID userId) throws NotFoundException;

    List<AnimalDetailDto> getDislikedAnimals(UUID userId) throws NotFoundException;

    List<AnimalDetailDto> getLikedAnimals(UUID userId) throws NotFoundException;

    List<AnimalDetailDto> getLikedSpecies(UUID userId, Species species) throws NotFoundException;

    void removeLikedAnimal(UUID userId, UUID animalId) throws NotFoundException;
}

