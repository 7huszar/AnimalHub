package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileContentStore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final String filesBasePath = "src/test/resources/demo data";

    private final String summary = "test summary";

    private final FileRepository fileRepository;
    private final FileContentStore fileContentStore;

    public void generateFiles() {
        if (!fileRepository.findAll().isEmpty()) {
            LOGGER.debug("files already generated");
        } else {
            LOGGER.debug("generating demo files from path: {}", filesBasePath);
            File filesDirectory = new File(filesBasePath);
            String absolutePath = filesDirectory.getAbsolutePath();
            if (!filesDirectory.isDirectory()) {
                throw new IllegalArgumentException("the given directory for demo files is not accessible!");
            }

            for (File f :
                filesDirectory.listFiles()) {
                try {
                    at.ac.tuwien.sepr.groupphase.backend.entity.File entity = at.ac.tuwien.sepr.groupphase.backend.entity.File.builder()
                        .name(f.getName().replace(".jpg", ""))
                        .summary(summary)
                        .contentMimeType(Files.probeContentType(f.toPath()))
                        .build();

                    at.ac.tuwien.sepr.groupphase.backend.entity.File savedFile = fileRepository.save(entity);

                    fileContentStore.setContent(savedFile, Files.newInputStream(f.toPath()));

                    // Save updated content-related information
                    fileRepository.save(savedFile);
                } catch (IOException e) {
                    // IOException of "probeContentType" must be handled!
                    throw new IllegalArgumentException("could not access content type of '" + f.getName() + "'");
                }
            }
        }
    }
}
