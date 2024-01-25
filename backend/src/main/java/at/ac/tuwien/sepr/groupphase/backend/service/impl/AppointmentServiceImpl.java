package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AppointmentMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AppointmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.AppointmentService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.conflict.AppointmentConflict;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.AppointmentValidator;
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
    private final AppointmentValidator appointmentValidator;
    private final AppointmentConflict appointmentConflict;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AnimalService animalService;


    @Override
    public AppointmentDto findById(UUID id) throws NotFoundException {
        LOGGER.trace("Find appointment by Id {}", id);

        return appointmentRepository.findById(id)
            .map(appointmentMapper::appointmentToAppointmentDto)
            .orElseThrow(() -> new NotFoundException("Appointment with id " + id + " does not exist."));
    }

    @Override
    public List<AppointmentDto> findByUserId(UUID userId) throws NotAuthorizedException, NotFoundException {
        LOGGER.trace("Find appointments of user by id {}", userId);

        // check user
        var user = userService.getCurrentUser();

        if (!user.getId().equals(userId)) {
            throw new NotAuthorizedException("User is authorized.");
        }

        return appointmentRepository.findByUserId(userId).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> findByShelterId(UUID shelterId) throws NotFoundException, NotAuthorizedException {
        LOGGER.trace("Find appointments of shelter by id {}", shelterId);

        // check if user is member of shelter
        var user = userService.getCurrentUser();

        if (!user.getShelter().getId().equals(shelterId)) {
            throw new NotAuthorizedException("User is not authorized.");
        }

        return appointmentRepository.findByShelterIdAndStatusNot(shelterId, AppointmentStatus.DECLINED).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> findByShelterIdWithStatus(UUID shelterId, AppointmentStatus status) throws NotFoundException, NotAuthorizedException {
        LOGGER.trace("Find appointments of shelter by id {} and status {}", shelterId, status);

        // check if user is member of shelter
        var user = userService.getCurrentUser();

        if (!user.getShelter().getId().equals(shelterId)) {
            throw new NotAuthorizedException("User is not authorized.");
        }

        return appointmentRepository.findByShelterIdAndStatus(shelterId, status).stream()
            .map(appointmentMapper::appointmentToAppointmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto create(AppointmentDto appointmentDto) throws ValidationException, NotFoundException, ConflictException {
        LOGGER.trace("Create new appointment: {}", appointmentDto);

        appointmentValidator.validateForCreate(appointmentDto);
        appointmentConflict.checkForCreate(appointmentDto);

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
    }

    @Override
    public AppointmentDto updateStatus(AppointmentDto appointmentDto) throws NotFoundException {
        LOGGER.trace("Update appointments {} status to {}", appointmentDto.getId(), appointmentDto.getStatus());

        var appointment = appointmentRepository.findById(appointmentDto.getId()).orElseThrow(NotFoundException::new);

        appointment.setStatus(appointmentDto.getStatus());

        appointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        LOGGER.trace("Delete appointment with id {}", id);

        appointmentRepository.findById(id).orElseThrow(NotFoundException::new);

        appointmentRepository.deleteById(id);
    }
}
