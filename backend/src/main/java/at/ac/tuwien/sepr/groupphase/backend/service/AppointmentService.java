package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    // TODO doku
    AppointmentDto findById(UUID id) throws NotFoundException;

    // TODO doku
    List<AppointmentDto> findByUserId(UUID userId) throws NotAuthorizedException, NotFoundException;

    // TODO doku
    List<AppointmentDto> findByShelterId(UUID shelterId) throws NotFoundException, NotAuthorizedException;

    // TODO doku
    List<AppointmentDto> findByShelterIdWithStatus(UUID shelterId, AppointmentStatus status) throws NotFoundException, NotAuthorizedException;

    // TODO doku
    AppointmentDto create(AppointmentDto appointmentDto) throws ValidationException, NotFoundException, ConflictException;

    // TODO doku
    AppointmentDto updateStatus(AppointmentDto appointmentDto) throws NotFoundException;

    // TODO doku
    void delete(UUID id) throws NotFoundException;
}
