package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileContentStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class FileRepositoryTest implements TestData {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileContentStore fileContentStore;


    // POSITIVE TESTS

    //    @Test
    //    public void createNewFileWithSuccess() {
    //
    //        File entity = File.builder()
    //            .name(TEST_IMAGE_NAME)
    //            .summary(TEST_IMAGE_SUMMARY)
    //            .build();
    //
    //
    //        File savedFile = fileRepository.save(entity);
    //
    //
    //        assertDoesNotThrow(() -> {
    //            fileContentStore.setContent(savedFile, new FileInputStream(TEST_FILE));
    //        });
    //
    //
    //        // Save updated content-related information
    //        File fileWithAllInformation = fileRepository.save(savedFile);
    //
    //
    //        assertAll(
    //            () -> assertEquals(1, fileRepository.findAll().size()),
    //            () -> assertEquals(entity.getName(), savedFile.getName()),
    //            () -> assertEquals(entity.getSummary(), savedFile.getSummary()),
    //            () -> assertNotNull(savedFile.getCreated()),
    //            () -> assertNotNull(savedFile.getId()),
    //            () -> assertNotNull(savedFile.getContentId()),
    //            () -> assertNotEquals(savedFile.getContentLength(), 0),
    //            () -> assertEquals(new FileInputStream(TEST_FILE), fileContentStore.getContent(savedFile))
    //            );
    //    }

}
