package at.ac.tuwien.sepr.groupphase.backend.service;


import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.UUID;

public interface PreferenceService {


    void like(UUID animalId) throws NotFoundException;
}
