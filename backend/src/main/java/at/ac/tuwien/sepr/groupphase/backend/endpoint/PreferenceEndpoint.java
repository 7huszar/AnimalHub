package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

@RestController
@RequestMapping(value = PreferenceEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class PreferenceEndpoint {

    static final String BASE_PATH = "/api/v1/preference";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PreferenceService preferenceService;

    /**
     * This method is responsible for handling POST requests to the "/api/v1/preference" endpoint.
     * It logs the request and delegates the action of liking an animal to the PreferenceService.
     *
     * @param animalId The UUID of the animal that is to be liked. This is passed in the request body
     */
    @PostMapping
    @Operation(summary = "Persisting like to the database", security = @SecurityRequirement(name = "apiKey"))
    public void like(@Valid @RequestBody UUID animalId, UUID userId) throws NotFoundException {
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("Body of request:\n{}", animalId);
        preferenceService.like(animalId, userId);
    }

}
