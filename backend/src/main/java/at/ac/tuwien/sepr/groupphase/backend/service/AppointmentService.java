package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentDto findById(UUID id);

    List<AppointmentDto> findByUserId(UUID userId);

    List<AppointmentDto> findByShelterId(UUID shelterId);

    List<AppointmentDto> findByShelterIdWithStatus(UUID id, AppointmentStatus status);

    AppointmentDto create(AppointmentDto appointmentDto);

    AppointmentDto updateStatus(AppointmentDto appointmentDto);

    void delete(UUID id);

    List<AppointmentDto> getUpcomingAppointments();
}
