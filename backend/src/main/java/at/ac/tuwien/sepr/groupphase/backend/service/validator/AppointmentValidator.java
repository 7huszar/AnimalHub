package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateForCreate(AppointmentDto appointmentDto) throws ValidationException {
        LOGGER.trace("validateForCreate({})", appointmentDto);
        var validationErrors = new LinkedList<String>();

        if (appointmentDto.getUser() != null) {
            validationErrors.add("No user set");
        }

        if (appointmentDto.getShelter() == null && appointmentDto.getAnimal() == null) {
            validationErrors.add("Shelter or animal must be set");
        }

        if (appointmentDto.getTimeFrom() == null || appointmentDto.getTimeTo() == null) {
            validationErrors.add("Time from/to must be set");
        } else {
            if (!appointmentDto.getTimeFrom().isBefore(appointmentDto.getTimeTo())) {
                validationErrors.add("timeFrom must be before timeTo");
            }

            if (appointmentDto.getTimeFrom().isBefore(Instant.now())) {
                validationErrors.add("timeFrom cannot be before now");
            }
        }

        if (appointmentDto.getTitle() == null) {
            validationErrors.add("Title must be set");
        } else if (appointmentDto.getTitle().length() > 255) {
            validationErrors.add("Title is too long");
        }

        if (appointmentDto.getMsg() == null) {
            validationErrors.add("Msg must be set");
        } else if (appointmentDto.getMsg().length() > 255) {
            validationErrors.add("Msg is too long");
        }

        if (appointmentDto.getType() == null) {
            validationErrors.add("Type must be set");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate data for creating an appointment", validationErrors);
        }
    }

}
