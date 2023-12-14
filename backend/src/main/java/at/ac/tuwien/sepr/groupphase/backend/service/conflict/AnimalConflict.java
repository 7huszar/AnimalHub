package at.ac.tuwien.sepr.groupphase.backend.service.conflict;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AnimalConflict {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShelterRepository shelterRepository;

    /**
     * Checks for any conflicts in system, if creating animal. <br>
     * Checks: <br>
     * - shelter of animal exists
     *
     * @param animalDto Details of animal to create
     * @throws ConflictException if any conflicts occur.
     */
    public void conflictForCreate(CreateAnimalDto animalDto) throws ConflictException {
        LOGGER.trace("conflictsForCreate({})", animalDto);
        List<String> conflictErrors = new ArrayList<>();

        if (shelterRepository.existsById(animalDto.getShelter().getId())) {
            conflictErrors.add("Shelter does not exist with id " + animalDto.getShelter().getId() + " does not exist.");
        }
        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Creating Animal creates conflict error", conflictErrors);
        }
    }
}
