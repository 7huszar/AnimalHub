package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper
public interface AppointmentMapper {

    Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto);

    AppointmentDto appointmentToAppointmentDto(Appointment appointment);
}
