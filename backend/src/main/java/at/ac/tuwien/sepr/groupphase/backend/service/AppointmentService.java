package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    // TODO doku
    AppointmentDto findById(UUID id);

    // TODO doku
    List<AppointmentDto> findByUserId(UUID userId);

    // TODO doku
    List<AppointmentDto> findByShelterId(UUID shelterId);

    // TODO doku
    List<AppointmentDto> findByShelterIdWithStatus(UUID id, AppointmentStatus status);

    // TODO doku
    AppointmentDto create(AppointmentDto appointmentDto);

    // TODO doku
    AppointmentDto updateStatus(AppointmentDto appointmentDto);

    // TODO doku
    void delete(UUID id);

    // TODO doku
    List<AppointmentDto> getUpcomingAppointments();
}
