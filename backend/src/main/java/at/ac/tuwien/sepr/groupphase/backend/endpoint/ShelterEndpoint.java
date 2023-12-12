package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos.*;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = ShelterEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class ShelterEndpoint {

    static final String BASE_PATH = "/api/v1/shelters";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShelterService shelterService;

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping
    @Operation(summary = "Get list of shelters", security = @SecurityRequirement(name = "apiKey"))
    public List<ShelterDto> findAll() {
        LOGGER.info("GET " + BASE_PATH);
        return shelterService.findAll();
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}")
    @Operation(summary = "Get shelter by id", security = @SecurityRequirement(name = "apiKey"))
    public ShelterDetailDto findById(@PathVariable UUID id) {
        LOGGER.info("GET " + BASE_PATH + "/ " + id);
        return shelterService.findById(id);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new animal in database", security = @SecurityRequirement(name = "apiKey"))
    public ShelterDto create(@Valid @RequestBody CreateShelterDto shelterDto) throws ValidationException {
        LOGGER.info("POST " + BASE_PATH + "/" + shelterDto.getName());
        LOGGER.debug("Body of request:\n{}", shelterDto);
        return shelterService.create(shelterDto);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("{id}")
    @Operation(summary = "Update specific shelter in database", security = @SecurityRequirement(name = "apiKey"))
    public ShelterDto update(@PathVariable UUID id, @Valid @RequestBody ShelterDto shelterDto) throws ValidationException {
        LOGGER.info("POST " + BASE_PATH + "/" + id);
        LOGGER.debug("Body of request:\n{}", shelterDto);
        shelterDto.setId(id);
        return shelterService.update(shelterDto);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @Operation(summary = "Get list of filtered shelters", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping("/search")
    @CrossOrigin(origins = "http://localhost:4200")
    public Stream<ShelterListDto> searchShelters(ShelterSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("request parameters: {}", searchParameters);
        return shelterService.search(searchParameters);
    }

    @DeleteMapping("{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        LOGGER.debug("###################################################" + id);
        shelterService.delete(UUID.fromString(id));
    }
}

