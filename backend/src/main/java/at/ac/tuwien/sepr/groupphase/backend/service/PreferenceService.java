package at.ac.tuwien.sepr.groupphase.backend.service;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.UUID;

public interface PreferenceService {


    void like(UUID animalId, UUID userId) throws NotFoundException;

    AnimalDetailDto[] getLikedAnimals(UUID userId) throws NotFoundException;
}

