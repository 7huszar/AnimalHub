package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.files.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileContentStore;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileRepository fileRepository;
    private final FileContentStore fileContentStore;
    private final FileValidator validator;
    private final FileMapper mapper;

    private final AnimalRepository animalRepository;

    @Override
    public FileDetailDto create(CreateAnimalFileDto fileDto) throws ValidationException, IOException {
        LOGGER.debug("Create new File: " + fileDto);

        validator.validateForCreate(fileDto);
        File entity = File.builder()
            .name(fileDto.getName())
            .summary(fileDto.getSummary())
            .contentMimeType(fileDto.getFile().getContentType())
            .build();

        File savedFile = fileRepository.save(entity);

        fileContentStore.setContent(savedFile, fileDto.getFile().getInputStream());

        // Save updated content-related information
        File fileWithAllInformation = fileRepository.save(savedFile);

        Animal animal = animalRepository.findById(fileDto.getAnimalId()).orElseThrow(() -> new NotFoundException("Animal with given ID does not exist."));

        animal.setProfilePicture(fileWithAllInformation);

        animalRepository.save(animal);

        // Create detailDto fr returning
        FileDetailDto detailDto = mapper.fileToFileDetailDto(fileWithAllInformation);

        // Get the actual file content as an InputStreamResource from the fileContentStore
        detailDto.setFile(new InputStreamResource(fileContentStore.getContent(fileWithAllInformation)));

        // return complete FileDetailDto
        return detailDto;
    }

    @Override
    public FileDetailDto findById(UUID id) {
        LOGGER.debug("Get File with id: " + id);

        File entity = fileRepository.findById(id).orElseThrow(
            () -> new NotFoundException("File does not exist.")
        );

        FileDetailDto detailDto = mapper.fileToFileDetailDto(entity);

        // Get the actual file content as an InputStreamResource from the fileContentStore
        detailDto.setFile(new InputStreamResource(fileContentStore.getContent(entity)));

        // return complete FileDetailDto
        return detailDto;
    }

}
