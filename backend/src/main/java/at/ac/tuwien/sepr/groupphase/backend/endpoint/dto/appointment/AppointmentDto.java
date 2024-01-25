package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentType;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDto {

    private UUID id;

    private UserDto user;

    private ShelterDto shelter;

    private AnimalDto animal;

    private Instant timeFrom;

    private Instant timeTo;

    private String title;

    private String msg;

    private AppointmentType type;

    private AppointmentStatus status;

    @Override
    public String toString() {
        return "AppointmentDto{"
            + "id=" + id
            + ", user=" + user
            + ", shelter=" + shelter
            + ", animal=" + animal
            + ", timeFrom=" + timeFrom
            + ", timeTo=" + timeTo
            + ", title='" + title + '\''
            + ", msg='" + msg + '\''
            + ", type=" + type
            + ", status=" + status
            + '}';
    }
}
