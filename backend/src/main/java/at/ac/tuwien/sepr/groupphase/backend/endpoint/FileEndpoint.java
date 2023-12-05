package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;


@RequestMapping(FileEndpoint.BASE_PATH)
@RequiredArgsConstructor
@RestController
public class FileEndpoint {

    static final String BASE_PATH = "/api/v1/files";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileService fileService;


    @PostMapping
    @Secured(UserRole.Values.USER_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileDetailDto create(@RequestParam("name") String name, @RequestParam("summary") String summary, @RequestParam("file") MultipartFile file) throws ValidationException, IOException {
        LOGGER.info("POST " + BASE_PATH + "/ " + name);
        CreateFileDto createDto = new CreateFileDto(name, summary, file);
        LOGGER.debug("Body of request:\n{}", createDto);
        return fileService.create(createDto);
    }


    @GetMapping("{fileId}")
    @Secured(UserRole.Values.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get file with specific id", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> findById(@PathVariable("fileId") Long id) {

        FileDetailDto fileDto = fileService.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(fileDto.getContentLength());
        headers.set("Content-Type", fileDto.getContentMimeType());

        return new ResponseEntity<Object>(fileDto.getFile(), headers, HttpStatus.OK);
    }
}