package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AppointmentMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AppointmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.AppointmentService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AnimalService animalService;

    @Override
    public AppointmentDto findById(UUID id) {
        LOGGER.trace("Find appointment by Id {}", id);

        // TODO validation

        return appointmentRepository.findById(id).map(appointmentMapper::appointmentToAppointmentDto).orElseThrow(/* TODO throw not found exception*/);
    }

    @Override
    public List<AppointmentDto> findByUserId(UUID userId) {
        LOGGER.trace("Find appointments of user by id {}", userId);

        // TODO validation

        return appointmentRepository.findByUserId(userId).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> findByShelterId(UUID shelterId) {
        LOGGER.trace("Find appointments of shelter by id {}", shelterId);

        // TODO validation

        return appointmentRepository.findByShelterIdAndStatusNot(shelterId, AppointmentStatus.DECLINED).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> findByShelterIdWithStatus(UUID id, AppointmentStatus status) {
        LOGGER.trace("Find appointments of shelter by id {} and status {}", id, status);

        // TODO validation

        return appointmentRepository.findByShelterIdAndStatus(id, status).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto create(AppointmentDto appointmentDto) {
        LOGGER.trace("Create new appointment: {}", appointmentDto);

        // TODO validate and conflict check

        try {
            var user = userService.getCurrentUser();

            // Set user and status (can not be controlled by client)
            appointmentDto.setUser(userMapper.userDetailDtoToUserDto(user));
            appointmentDto.setStatus(AppointmentStatus.PENDING);

            // If animal is present set its shelter
            if (appointmentDto.getAnimal() != null) {
                var animal = animalService.findById(appointmentDto.getAnimal().getId());
                appointmentDto.setShelter(animal.getShelter());
            }

            var entity = appointmentMapper.appointmentDtoToAppointment(appointmentDto);

            entity = appointmentRepository.save(entity);

            return appointmentMapper.appointmentToAppointmentDto(entity);
        } catch (NotFoundException e) {
            throw new RuntimeException(e); // TODO handle
        }
    }

    @Override
    public AppointmentDto updateStatus(AppointmentDto appointmentDto) {
        LOGGER.trace("Update appointments {} status to {}", appointmentDto.getId(), appointmentDto.getStatus());

        // TODO validate

        try {
            var appointment = appointmentRepository.findById(appointmentDto.getId()).orElseThrow(NotFoundException::new); // TODO throw notfound

            appointment.setStatus(appointmentDto.getStatus());

            appointment = appointmentRepository.save(appointment);

            return appointmentMapper.appointmentToAppointmentDto(appointment);

        } catch (NotFoundException e) {
            throw new RuntimeException(e); // TODO handle
        }
    }

    @Override
    public void delete(UUID id) {
        LOGGER.trace("Delete appointment with id {}", id);

        // TODO throw not found

        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentDto> getUpcomingAppointments() {
        return null; // TODO implement
    }
}
