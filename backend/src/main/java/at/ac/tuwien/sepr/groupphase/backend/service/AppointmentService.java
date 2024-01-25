package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;

/**
 * The interface defining the operations for managing appointments.
 */
public interface AppointmentService {

    /**
     * Finds an appointment by its unique identifier.
     *
     * @param id The unique identifier of the appointment.
     * @return The AppointmentDto representing the found appointment.
     * @throws NotFoundException If the appointment with the given ID is not found.
     */
    AppointmentDto findById(UUID id) throws NotFoundException;

    /**
     * Finds all appointments associated with a user by their unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return A list of AppointmentDto representing the user's appointments.
     * @throws NotAuthorizedException If the user is not authorized to access the appointments.
     * @throws NotFoundException      If no appointments are found for the given user.
     */
    List<AppointmentDto> findByUserId(UUID userId) throws NotAuthorizedException, NotFoundException;

    /**
     * Finds all appointments associated with a shelter by its unique identifier.
     *
     * @param shelterId The unique identifier of the shelter.
     * @return A list of AppointmentDto representing the shelter's appointments.
     * @throws NotFoundException      If no appointments are found for the given shelter.
     * @throws NotAuthorizedException If the user is not authorized to access the shelter's appointments.
     */
    List<AppointmentDto> findByShelterId(UUID shelterId) throws NotFoundException, NotAuthorizedException;

    /**
     * Finds appointments associated with a shelter filtered by status.
     *
     * @param shelterId The unique identifier of the shelter.
     * @param status    The status by which to filter the appointments.
     * @return A list of AppointmentDto representing the shelter's appointments with the specified status.
     * @throws NotFoundException      If no appointments are found for the given shelter and status.
     * @throws NotAuthorizedException If the user is not authorized to access the shelter's appointments.
     */
    List<AppointmentDto> findByShelterIdWithStatus(UUID shelterId, AppointmentStatus status) throws NotFoundException, NotAuthorizedException;

    /**
     * Creates a new appointment.
     *
     * @param appointmentDto The AppointmentDto containing information for creating the appointment.
     * @return The created AppointmentDto.
     * @throws ValidationException If the provided appointment data is not valid.
     * @throws NotFoundException   If referenced entities (e.g., user or shelter) are not found.
     * @throws ConflictException   If there is a conflict preventing the creation of the appointment.
     */
    AppointmentDto create(AppointmentDto appointmentDto) throws ValidationException, NotFoundException, ConflictException;

    /**
     * Updates the status of an existing appointment.
     *
     * @param appointmentDto The AppointmentDto containing the updated status and appointment information.
     * @return The updated AppointmentDto.
     * @throws NotFoundException If the appointment to be updated is not found.
     */
    AppointmentDto updateStatus(AppointmentDto appointmentDto) throws NotFoundException;

    /**
     * Deletes an appointment by its unique identifier.
     *
     * @param id The unique identifier of the appointment to be deleted.
     * @throws NotFoundException If the appointment with the given ID is not found.
     */
    void delete(UUID id) throws NotFoundException;
}
