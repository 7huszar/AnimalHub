package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalRankingDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.preference.PreferenceRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.PreferenceService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
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
     * @param request The UUID of the animal and the user. This is passed in the request body
     */
    @PostMapping
    @Operation(summary = "Persisting like to the database", security = @SecurityRequirement(name = "apiKey"))
    public void like(@Valid @RequestBody PreferenceRequestDto request) throws NotFoundException {
        UUID animalId = request.getAnimalId();
        UUID userId = request.getUserId();
        boolean like = request.isLike();
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("Body of request:\n{}\n{}", animalId, userId);
        if (like) {
            preferenceService.like(animalId, userId);
        } else {
            preferenceService.dislike(animalId, userId);
        }
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get recommended animals", security = @SecurityRequirement(name = "apiKey"))
    public List<AnimalRankingDto> getRecommendedAnimals(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH);
        return preferenceService.getRecommendedAnimals(id);
    }

    @GetMapping("{id}/{like}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get liked animals", security = @SecurityRequirement(name = "apiKey"))
    public List<AnimalDetailDto> getLikedDislikedAnimals(@PathVariable UUID id, @PathVariable boolean like) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH);
        if (like) {
            return preferenceService.getLikedAnimals(id);
        } else {
            return preferenceService.getDislikedAnimals(id);
        }
    }

    @DeleteMapping("{userId}/{animalId}")
    @Operation(summary = "Removes animal from likedDisliked", security = @SecurityRequirement(name = "apiKey"))
    public void removeLikedAnimal(@PathVariable UUID userId, @PathVariable UUID animalId) throws NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/{}/{}", userId, animalId);

        preferenceService.removeLikedAnimal(userId, animalId);
    }


}
