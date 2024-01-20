package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
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

    /**
     * Find all animals which are currently stored in the database.
     *
     * @return List of AnimalDetailDtos for all the animals
     */
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of animals", security = @SecurityRequirement(name = "apiKey"))
    public List<AnimalDetailDto> findAll() {
        LOGGER.info("GET " + BASE_PATH);
        return animalService.findAll();
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("search")
    @Operation(summary = "Search for specific animals", security = @SecurityRequirement(name = "apiKey"))
    public List<AnimalDetailDto> search(@Valid @RequestBody AnimalSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH + "/search/{}", searchParameters);
        return animalService.search(searchParameters);
    }


    /**
     * Find animal by specific id (mainly used for animal-homepage).
     *
     * @param id id
     * @return an AnimalDetailDto containing all the data of the specific animal,
     *          except its profile picture which has to be fetched separately (via getProfileImg)
     * @throws NotFoundException when the id points to no existing animal
     */
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get animal with specific id", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto findById(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/{}", id);
        return animalService.findById(id);
    }

    /**
     * create a new animal in the persistent data storage. (profile img has to be stored via setProfileImg).
     *
     * @param createDto createDto
     * @return an AnimalDetailDto containing all the data of the specific animal,
     *          except its profile picture which has to be fetched separately (via getProfileImg)
     * @throws ValidationException when the validation for this new animal failed (empty name, etc.)
     * @throws ConflictException   when there is a conflict with the already existing data and this new animal (shelter does not exist.)
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto create(@Valid @RequestBody CreateAnimalDto createDto) throws ValidationException, ConflictException {
        LOGGER.info("POST " + BASE_PATH + "/ " + createDto.getName());
        LOGGER.debug("Body of request:\n{}", createDto);
        return animalService.create(createDto);
    }

    /**
     * updates an existing animal in the database with the given id and overwrites the values with the ones in animalDto.
     *
     * @param id        id
     * @param animalDto values to update animal with
     * @return an AnimalDetailDto containing all the data of the specific animal,
     *          except its profile picture which has to be fetched separately (via getProfileImg)
     * @throws ValidationException when the update values fail to validate (empty name, etc.)
     * @throws NotFoundException   when the id points to no existing animal in the database
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    @Operation(summary = "Update specific animal in database", security = @SecurityRequirement(name = "apiKey"))
    public AnimalDetailDto update(@PathVariable UUID id, @Valid @RequestBody AnimalDetailDto animalDto) throws ValidationException, NotFoundException {
        LOGGER.info("POST " + BASE_PATH + "/" + id);
        LOGGER.debug("Body of request:\n{}", animalDto);
        animalDto.setId(id);
        return animalService.update(animalDto);
    }

    /**
     * deletes the animal with the given id.
     *
     * @param id id
     * @throws NotFoundException when the id points to no existing animal
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @DeleteMapping("{id}")
    @Operation(summary = "Deletes specific animal in database", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        animalService.delete(id);
    }

    /**
     * Get the profile image of the animal with the given id.
     *
     * @param id id
     * @return respond with the File Entity in the HttpResponse
     * @throws NotFoundException when the id points to no existing animal
     */
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/profileImg")
    @Operation(summary = "Get animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProfileImage(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/{}/files/profileImg", id);
        var fileDto = animalService.getProfileImage(id);

        var headers = new HttpHeaders();
        headers.set("Content-Type", fileDto.getContentMimeType());
        headers.setContentLength(fileDto.getContentLength());

        return new ResponseEntity<>(fileDto.getFile(), headers, HttpStatus.OK);
    }

    /**
     * Set the profile image for the animal with the given id.
     *
     * @param id      id
     * @param name    name
     * @param summary summary
     * @param file    file as MultiPartFile
     * @return FileDto (which does not contain the actual image file, but rather the metadata of the newly stored image)
     * @throws ValidationException when the data given fails validation (empty name, etc.)
     * @throws ConflictException   when the data conflicts with existing data (profile image already set)
     * @throws IOException         when there is a problem with actually storing or accessing the image file
     * @throws NotFoundException   when the id points to no existing animal
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @PostMapping("{id}/files/profileImg")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sets animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto setProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file)
        throws ValidationException, ConflictException, IOException, NotFoundException {
        LOGGER.info("POST " + BASE_PATH + "/{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.setProfileImage(id, createFileDto);
    }

    /**
     * Update the profile image for the animal with the given id.
     *
     * @param id      id
     * @param name    name
     * @param summary summary
     * @param file    file as MultiPartFile
     * @return FileDto (which does not contain the actual image file, but rather the metadata of the newly stored image)
     * @throws ValidationException when the data given fails validation (empty name, etc.)
     * @throws IOException         when there is a problem with actually storing or accessing the image file
     * @throws NotFoundException   when the id points to no existing animal
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("{id}/files/profileImg")
    @Operation(summary = "Updates animal's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto updateProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException, NotFoundException {
        LOGGER.info("PUT " + BASE_PATH + "/{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.updateProfileImage(id, createFileDto);
    }

    /**
     * Get the contents of a specific animal which have been uploaded to the database previously.
     *
     * @param id id
     * @return List of FileDtos which do not actually contain the image file but its metadata
     * @throws NotFoundException when the id points to no existing animal
     */
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/content")
    @Operation(summary = "Get content information about animal", security = @SecurityRequirement(name = "apiKey"))
    public List<FileDto> getContents(@PathVariable UUID id) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/{}/files", id);

        return animalService.getContents(id);
    }

    /**
     * Get a specific piece of content from a specific animal.
     *
     * @param id     id
     * @param fileId fileId
     * @return HttpResponse which contains the actual image file
     * @throws NotFoundException when the id points to no existing animal, or to no existing file
     */
    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/content/{fileId}")
    @Operation(summary = "Get content of animal", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getContent(@PathVariable UUID id, @PathVariable UUID fileId) throws NotFoundException {
        LOGGER.info("GET " + BASE_PATH + "/{}/files/{}", id, fileId);
        var fileDto = animalService.getContent(id, fileId);

        var headers = new HttpHeaders();
        headers.set("Content-Type", fileDto.getContentMimeType());
        headers.setContentLength(fileDto.getContentLength());

        return new ResponseEntity<>(fileDto.getFile(), headers, HttpStatus.OK);
    }

    /**
     * Add a specific image or video to the content list of a specific animal.
     *
     * @param id      id
     * @param name    name
     * @param summary summary
     * @param file    file
     * @return FileDto which does not actually contain the image file but its metadata
     * @throws ValidationException when the data given fails validation (empty name, etc.)
     * @throws IOException         when there is a problem with actually storing or accessing the image file
     * @throws NotFoundException   when the id points to no existing animal
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @PostMapping("{id}/files/content")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add content to animal", security = @SecurityRequirement(name = "apiKey"))
    public FileDto addContent(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException, NotFoundException {
        LOGGER.info("POST " + BASE_PATH + "/{}/files", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return animalService.addContent(id, createFileDto);
    }

    /**
     * Delete a specific piece of content from an animal.
     *
     * @param id     id
     * @param fileId fileId
     * @throws NotFoundException when the id points to no existing animal, or to no existing file
     */
    @Secured(UserRole.Values.MEMBER_VALUE)
    @DeleteMapping("{id}/files/content/{fileId}")
    @Operation(summary = "Deletes content of animal", security = @SecurityRequirement(name = "apiKey"))
    public void deleteContent(@PathVariable UUID id, @PathVariable UUID fileId) throws NotFoundException {
        LOGGER.info("DELETE " + BASE_PATH + "/{}/files/{}", id, fileId);

        animalService.deleteContent(id, fileId);
    }
}
