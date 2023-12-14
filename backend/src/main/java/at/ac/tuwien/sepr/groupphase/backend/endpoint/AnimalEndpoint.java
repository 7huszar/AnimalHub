package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Secured(UserRole.Values.MEMBER_VALUE)
    @DeleteMapping("{id}")
    @Operation(summary = "Deletes specific animal in database", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable UUID id) {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        animalService.delete(id);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/profileImg")
    @Operation(summary = "Get animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProfileImage(@PathVariable UUID id) {
        LOGGER.info("GET " + BASE_PATH + "/{}/files/profileImg", id);
        var fileDto = animalService.getProfileImage(id);

        var headers = new HttpHeaders();
        headers.set("Content-Type", fileDto.getContentMimeType());
        headers.setContentLength(fileDto.getContentLength());

        return new ResponseEntity<>(fileDto.getFile(), headers, HttpStatus.OK);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PostMapping("{id}/files/profileImg")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sets animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto setProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, ConflictException, IOException {
        LOGGER.info("POST " + BASE_PATH + "/{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.setProfileImage(id, createFileDto);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("{id}/files/profileImg")
    @Operation(summary = "Updates animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto updateProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException {
        LOGGER.info("PUT " + BASE_PATH + "/{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.updateProfileImage(id, createFileDto);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/content")
    @Operation(summary = "Get content information about animal", security = @SecurityRequirement(name = "apiKey"))
    public List<FileDto> getContents(@PathVariable UUID id) {
        LOGGER.info("GET " + BASE_PATH + "/{}/files", id);

        return animalService.getContents(id);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/content/{fileId}")
    @Operation(summary = "Get content of animal", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getContent(@PathVariable UUID id, @PathVariable UUID fileId) {
        LOGGER.info("GET " + BASE_PATH + "/{}/files/{}", id, fileId);
        var fileDto = animalService.getContent(id, fileId);

        var headers = new HttpHeaders();
        headers.set("Content-Type", fileDto.getContentMimeType());
        headers.setContentLength(fileDto.getContentLength());

        return new ResponseEntity<>(fileDto.getFile(), headers, HttpStatus.OK);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PostMapping("{id}/files/content")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add content to animal", security = @SecurityRequirement(name = "apiKey"))
    public FileDto addContent(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException {
        LOGGER.info("POST " + BASE_PATH + "/{}/files", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.addContent(id, createFileDto);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @DeleteMapping("{id}/files/content/{fileId}")
    @Operation(summary = "Deletes content of animal", security = @SecurityRequirement(name = "apiKey"))
    public void deleteContent(@PathVariable UUID id, @PathVariable UUID fileId) {
        LOGGER.info("DELETE " + BASE_PATH + "/{}/files/{}", id, fileId);

        animalService.deleteContent(id, fileId);
    }
}
