package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
//// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
//// the entire application context
@SpringBootTest
@ActiveProfiles("test")
public class FileServiceTest extends TestBase {

    @Autowired
    FileService fileService;

    @Test
    public void findAllSuccessfully() throws ValidationException, ConflictException {
        List<FileDetailDto> result = fileService.findAll();

        assertAll(
            () -> assertEquals(result.size(), allFileEntities.size())
        );
    }


    @Test
    public void createNewFileWithSuccess() throws ValidationException, ConflictException, NotFoundException {

        try {
            System.out.println("CURRENT DIRECTORY: " + System.getProperty("user.dir"));
            System.out.println("FILE DIRECTORY ABSOLUTE PATH: " + new java.io.File("src/test/resources/demo data").getAbsolutePath());


            assertFalse(new MockMultipartFile(TEST_FILE_FILE.getName(), new FileInputStream(TEST_FILE_FILE)).isEmpty());

            assertFalse(Arrays.stream(new File("src/test/resources/demo data").listFiles()).findFirst().orElseThrow(() -> new IllegalArgumentException("the given directory for demo files is not accessible!")) == null);

            System.out.println("MOCKMULTIPARTFILE: " + new MockMultipartFile(TEST_FILE_FILE.getName(), new FileInputStream(TEST_FILE_FILE)));

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

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("the given directory for demo files is not accessible!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}