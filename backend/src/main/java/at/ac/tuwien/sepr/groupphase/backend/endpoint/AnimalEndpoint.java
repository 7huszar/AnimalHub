package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
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

@RestController
@RequestMapping(value = "/api/v1/animals")
@RequiredArgsConstructor
public class AnimalEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalService animalService;

    @Secured({"ROLE_ADMIN","ROLE_SHELTER_USER","ROLE_EXTERNAL_USER"})
    @GetMapping
    @Operation(summary = "Get list of animals", security = @SecurityRequirement(name = "apiKey"))
    public List<Animal> findAll() {
        LOGGER.info("GET /api/v1/animals");
        return animalService.findAll();
    }

    @Secured({"ROLE_ADMIN","ROLE_SHELTER_USER"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto create(@Valid @RequestBody CreateAnimalDto animalDto){
        LOGGER.info("POST /api/v1/animals/create " + animalDto);
        return animalService.create(animalDto);
    }

}
