package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FileServiceTest extends TestBase {

    @Autowired
    FileService fileService;

    // function - FIND ALL
    // POSITIVE TEST(S)
    @Test
    public void findAllSuccessfully() {
        List<FileDetailDto> result = fileService.findAll();

        assertAll(
            () -> assertEquals(result.size(), allFileEntities.size())
        );
    }

    // function - CREATE
    // POSITIVE TEST(S)
    @Test
    public void createNewFileSuccessfully() throws ValidationException, NotFoundException, IOException {

        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            .file(new MockMultipartFile(TEST_FILE_FILE.getName(), TEST_FILE_FILE.getName(), Files.probeContentType(Path.of(TEST_FILE_FILE.getAbsolutePath())), new FileInputStream(TEST_FILE_FILE)))
            .build();

        FileDetailDto newFile = fileService.findById(fileService.create(createFileDto).getId());

        assertAll(
            () -> assertEquals(allFileEntities.size() + 1, fileService.findAll().size()),
            () -> assertEquals(newFile.getName(), createFileDto.getName()),
            () -> assertEquals(newFile.getSummary(), createFileDto.getSummary()),
            () -> assertNotNull(newFile.getFile()),
            () -> assertArrayEquals(newFile.getFile().getInputStream().readAllBytes(), createFileDto.getFile().getInputStream().readAllBytes())
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void createNewFileWithoutNameThrowsValidationException() {
        CreateFileDto createFileDto = CreateFileDto.builder()
            .name(TEST_FILE_NAME)
            .summary(TEST_FILE_SUMMARY)
            // EMPTY FILE
            .build();

        assertThrows(ValidationException.class, () -> fileService.create(createFileDto));
    }

    // function - FIND BY ID
    // POSITIVE TEST(S)

    @Test
    public void findByIdSuccessfully() throws NotFoundException {
        File file = allFileEntities.get(0);
        FileDetailDto returnFile = fileService.findById(allFileEntities.get(0).getId());

        assertAll(
            () -> assertNotNull(returnFile),
            () -> assertEquals(file.getName(), returnFile.getName()),
            () -> assertEquals(file.getSummary(), returnFile.getSummary()),
            () -> assertNotNull(returnFile.getFile()),
            () -> assertEquals(returnFile.getContentLength(), file.getContentLength())
        );
    }

    // NEGATIVE TEST(S)

    @Test
    public void findByIdWithNonExistingIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> fileService.findById(UUID.randomUUID()));
    }

    // function - DELETE
    // POSITIVE TEST(S)

    @Test
    public void deleteSuccessfully() throws NotFoundException {
        File demoFile1 = allFileEntities.stream().filter(f -> f.getName().contains("file demo 1")).findFirst().orElseThrow();
        UUID demoFile1Id = demoFile1.getId();
        fileService.delete(demoFile1.getId());

        assertAll(
            () -> assertEquals(fileService.findAll().size(), allFileEntities.size() - 1),
            () -> assertThrows(NotFoundException.class, () -> fileService.findById(demoFile1Id))
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void deleteNonExistingFileUnsuccessfully() {
        assertThrows(NotFoundException.class, () -> fileService.delete(UUID.randomUUID()));
    }

}