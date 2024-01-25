package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.appointment.AppointmentDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = AppointmentEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class AppointmentEndpoint {

    static final String BASE_PATH = "api/v1/appointments";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AppointmentService appointmentService;

    // TODO doku
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public AppointmentDto findById(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/" + id);
        return appointmentService.findById(id);
    }

    // TODO doku
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("user/{id}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public List<AppointmentDto> findByUserId(@PathVariable UUID id) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("GET " + BASE_PATH + "/user/" + id);
        return appointmentService.findByUserId(id);
    }

    // TODO doku
    @Secured(UserRole.Values.MEMBER_VALUE)
    @GetMapping("shelter/{id}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public List<AppointmentDto> findByShelterId(@PathVariable UUID id) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("GET " + BASE_PATH + "/shelter/" + id);
        return appointmentService.findByShelterId(id);
    }

    // TODO doku
    @Secured(UserRole.Values.MEMBER_VALUE)
    @GetMapping("shelter/{id}/{status}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public List<AppointmentDto> findByShelterIdWithStatus(@PathVariable UUID id, @PathVariable AppointmentStatus status) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("GET " + BASE_PATH + "/shelter/" + id + "/" + status);
        return appointmentService.findByShelterIdWithStatus(id, status);
    }

    // TODO doku
    @Secured(UserRole.Values.USER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public AppointmentDto create(@RequestBody AppointmentDto appointmentDto) throws ValidationException, ConflictException, NotFoundException {
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("Body of request:\n{}", appointmentDto);
        return appointmentService.create(appointmentDto);
    }

    // TODO doku
    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("{id}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public AppointmentDto updateStatus(@PathVariable UUID id, @RequestBody AppointmentDto appointmentDto) throws NotFoundException {
        LOGGER.info("PUT " + BASE_PATH + "/" + id);
        return appointmentService.updateStatus(appointmentDto);
    }

    // TODO doku
    @Secured(UserRole.Values.USER_VALUE)
    @DeleteMapping("{id}")
    @Operation(summary = "", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/" + id);
        appointmentService.delete(id);
    }
}
