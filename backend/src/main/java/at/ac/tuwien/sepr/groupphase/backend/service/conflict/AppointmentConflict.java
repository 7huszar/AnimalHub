package at.ac.tuwien.sepr.groupphase.backend.service.conflict;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class AppointmentConflict {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final AnimalRepository animalRepository;

    public void checkForCreate(AppointmentDto appointmentDto) throws ConflictException {
        LOGGER.trace("checkForCreate({})", appointmentDto);
        var conflictErrors = new ArrayList<String>();

        if (appointmentDto.getUser() != null && !userRepository.existsById(appointmentDto.getUser().getId())) {
            conflictErrors.add("User does not exist");
        }

        if (appointmentDto.getShelter() != null && !shelterRepository.existsById(appointmentDto.getShelter().getId())) {
            conflictErrors.add("Shelter does not exist");
        }

        if (appointmentDto.getAnimal() != null && !animalRepository.existsById(appointmentDto.getAnimal().getId())) {
            conflictErrors.add("Animal does not exist");
        }

        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Creating the appointment creates conflict errors", conflictErrors);
        }
    }

}
