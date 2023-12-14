package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConflictValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalShelterMapper mapper;
    private final ShelterRepository shelterRepository;


    private final UserRepository userRepository;

    public void checkRegisterConflicts(String email) throws ConflictException {
        LOGGER.debug("checkRegisterConflicts({})", email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Application user already registered by the name %s".formatted(email));
        }
    }


    Boolean shelterExists(AnimalDetailDto animalDto) {
        Optional<Shelter> foundShelter = shelterRepository.findById(animalDto.getShelter().getId());
        return foundShelter.isPresent();
    }

    void validateAnimalDetails(AnimalDetailDto animalDto) throws ConflictException {
        LOGGER.trace("validateAnimalDetails({})", animalDto);
        List<String> conflictErrors = new ArrayList<>();

        if (animalDto.getShelter() == null || !shelterExists(animalDto)) {
            conflictErrors.add("Shelter does not exist. Cannot create animal");
        }
        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Creating Animal creates conflict error", conflictErrors);
        }
    }

    public void validateForCreate(CreateAnimalDto animalDto) throws ConflictException {
        LOGGER.trace("validateForCreate({})", animalDto);
        validateAnimalDetails(mapper.createAnimalDtoToAnimalDetailDto(animalDto));
    }

    public void checkUserDeleteConflicts(UUID id) throws ConflictException {
        LOGGER.trace("checkUserDeleteConflicts({})", id);

        if (userRepository.findApplicationUserById(id) == null) {
            throw new NotFoundException("User does not exist");
        } else {
            ApplicationUser user = userRepository.findApplicationUserById(id);
            if (user.getRole().equals(UserRole.ADMIN)) {
                throw new ConflictException("Cannot delete another Admin");

            }
        }

    }
}




