package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import com.fasterxml.jackson.databind.ser.Serializers;
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

@RestController
@RequestMapping(value = AnimalEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class AnimalEndpoint {

    static final String BASE_PATH = "/api/v1/animals";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalService animalService;

    @Secured({"ROLE_EXTERNAL_USER"})
    @GetMapping
    @Operation(summary = "Get list of animals", security = @SecurityRequirement(name = "apiKey"))
    public List<Animal> findAll() {
        LOGGER.info("GET " + BASE_PATH);
        return animalService.findAll();
    }

    @Secured({"ROLE_SHELTER_USER"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto create(@Valid @RequestBody CreateAnimalDto animalDto) {
        LOGGER.info("POST " + BASE_PATH + "/ " + animalDto.getName());
        LOGGER.debug("Body of request:\n{}", animalDto);

        return animalService.create(animalDto);
    }

    @Secured({"ROLE_SHELTER_USER"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    @Operation(summary = "Update specific animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto update(@PathVariable UUID id, @Valid @RequestBody AnimalDetailDto animalDto) {
        LOGGER.info("POST " + BASE_PATH + "/" + id);
        LOGGER.debug("Body of request:\n{}", animalDto);
        animalDto.setId(id);
        return animalService.update(animalDto);
    }

}
