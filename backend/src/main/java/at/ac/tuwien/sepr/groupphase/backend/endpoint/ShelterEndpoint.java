package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
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
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        LOGGER.info("DELETE " + BASE_PATH + "/{}", id);
        shelterService.delete(id);
    }

    @Secured(UserRole.Values.USER_VALUE)
    @GetMapping("{id}/files/profileImg")
    @Operation(summary = "Get shelter's profile image", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProfileImage(@PathVariable UUID id) {
        LOGGER.info("GET " + BASE_PATH + "/{}/files/profileImg", id);
        var fileDto = shelterService.getProfileImage(id);

        var headers = new HttpHeaders();
        headers.set("Content-Type", fileDto.getContentMimeType());
        headers.setContentLength(fileDto.getContentLength());

        return new ResponseEntity<>(fileDto.getFile(), headers, HttpStatus.OK);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PostMapping("{id}/files/profileImg")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sets shelter's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto setProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, ConflictException, IOException {
        LOGGER.info("POST " + BASE_PATH + "/{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return shelterService.setProfileImage(id, createFileDto);
    }

    @Secured(UserRole.Values.MEMBER_VALUE)
    @PutMapping("{id}/files/profileImg")
    @Operation(summary = "Updates shelter's profile image", security = @SecurityRequirement(name = "apiKey"))
    public FileDto updateProfileImage(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException {
        LOGGER.info("PUT " + BASE_PATH + "{}/files/profileImg", id);
        var createFileDto = new CreateFileDto(name, summary, file);

        return shelterService.updateProfileImage(id, createFileDto);
    }

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("/search")
    @Operation(summary = "Get list of filtered shelters", security = @SecurityRequirement(name = "apiKey"))
    public List<ShelterListDto> searchShelters(ShelterSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("request parameters: {}", searchParameters);
        return shelterService.search(searchParameters);
    }
}

