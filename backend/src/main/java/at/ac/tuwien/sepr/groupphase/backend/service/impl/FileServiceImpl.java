package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileRepository fileRepository;
    // private final FileContentStore fileContentStore;
    private final FileValidator validator;
    private final FileMapper mapper;

    @Override
    public FileDto create(CreateFileDto fileDto) throws ValidationException, IOException {
        LOGGER.debug("Create new File: " + fileDto);

        validator.validateForCreate(fileDto);
        var entity = File.builder()
            .name(fileDto.getName())
            .summary(fileDto.getSummary())
            .contentMimeType(fileDto.getFile().getContentType())
            .build();

        var savedFile = fileRepository.save(entity);

        // fileContentStore.setContent(savedFile, fileDto.getFile().getInputStream());

        // Save updated content-related information
        savedFile = fileRepository.save(savedFile);

        // return complete FileDto
        return mapper.fileToFileDto(savedFile);
    }

    @Override
    public FileDetailDto findById(UUID id) throws NotFoundException {
        LOGGER.debug("Get File with id: " + id);

        File entity = fileRepository.findById(id).orElseThrow(
            () -> new NotFoundException("File does not exist.")
        );

        FileDetailDto detailDto = mapper.fileToFileDetailDto(entity);

        // Get the actual file content as an InputStreamResource from the fileContentStore
        // detailDto.setFile(new InputStreamResource(fileContentStore.getContent(entity)));

        // return complete FileDetailDto
        return detailDto;
    }

    @Override
    public List<FileDetailDto> findAll() {
        LOGGER.debug("Find all Files");

        List<FileDetailDto> returnList = Collections.emptyList();


        fileRepository.findAll().forEach((file) -> {
            FileDetailDto dto = mapper.fileToFileDetailDto(file);
            // dto.setFile(new InputStreamResource(fileContentStore.getContent(file)));
            returnList.add(dto);
        });

        return returnList;
    }
}
