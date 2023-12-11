package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@RequestMapping(value = AnimalEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class AnimalEndpoint {

    static final String BASE_PATH = "/api/v1/animals";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalService animalService;

    // todo doku
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of animals", security = @SecurityRequirement(name = "apiKey"))
    public List<AnimalDetailDto> findAll() {
        LOGGER.info("GET " + BASE_PATH);
        return animalService.findAll();
    }

    // todo doku
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get animal with specific id", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto findById(@PathVariable UUID id) {
        LOGGER.info("GET " + BASE_PATH + "/{}", id);
        return animalService.findById(id);
    }

    // todo doku
    @Secured(UserRole.Values.MEMBER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto create(@Valid @RequestBody CreateAnimalDto animalDto) throws ValidationException, ConflictException {
        LOGGER.info("POST " + BASE_PATH + "/ " + animalDto.getName());
        LOGGER.debug("Body of request:\n{}", animalDto);
        return animalService.create(animalDto);
    }

    // todo doku
    @Secured(UserRole.Values.MEMBER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    @Operation(summary = "Update specific animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto update(@PathVariable UUID id, @Valid @RequestBody AnimalDetailDto animalDto) throws ValidationException {
        LOGGER.info("POST " + BASE_PATH + "/" + id);
        LOGGER.debug("Body of request:\n{}", animalDto);
        animalDto.setId(id);
        return animalService.update(animalDto);
    }

    /**
     * deletes the animal with the given id.
     *
     * @param id id of animal-to-delete
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        animalService.delete(id);
    }


}
