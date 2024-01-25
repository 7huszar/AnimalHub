package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.conflict.AnimalConflict;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.AnimalValidator;
import at.ac.tuwien.sepr.groupphase.backend.util.ImageBlur;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileService fileService;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final AnimalRepository animalRepository;
    private final AnimalShelterMapper mapper;
    private final AnimalValidator validator;
    private final AnimalConflict conflict;

    @Override
    public List<AnimalDetailDto> findAll() {
        LOGGER.trace("Find all Animals");

        return animalRepository.findAll().stream()
            .map(mapper::animalToAnimalDetailDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AnimalDetailDto> search(AnimalSearchDto searchParameters) {
        LOGGER.trace("Search for animals which fulfill these parameters {}", searchParameters);

        return animalRepository.findAnimalsByParameters(
                searchParameters.getName(),
                searchParameters.getGender(),
                searchParameters.getSpecies(),
                searchParameters.getRace(),
                searchParameters.getBornBefore(),
                searchParameters.getBornAfter(),
                searchParameters.getHeavierThan(),
                searchParameters.getLighterThan(),
                searchParameters.getTallerThan(),
                searchParameters.getSmallerThan()
            ).stream()
            .map(mapper::animalToAnimalDetailDto)
            .collect(Collectors.toList());
    }

    @Override
    public AnimalDetailDto create(CreateAnimalDto animalDto) throws ValidationException, ConflictException {
        LOGGER.trace("Create new Animal: " + animalDto);

        conflict.conflictForCreate(animalDto);
        validator.validateForCreate(animalDto);


        Animal entity = mapper.createAnimalDtoToAnimal(animalDto);

        entity = animalRepository.save(entity);
        return mapper.animalToAnimalDetailDto(entity);
    }

    @Override
    public AnimalDetailDto update(AnimalDetailDto animalDto) throws ValidationException, NotFoundException {
        LOGGER.trace("Update Animal with id: " + animalDto.getId() + ", with following data:" + animalDto);

        validator.validateForUpdate(animalDto);
        Animal newEntity = mapper.animalDetailDtoToAnimal(animalDto);

        // Get old entity for content collection
        Animal oldEntity = animalRepository.findById(newEntity.getId()).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        newEntity.setProfilePicture(oldEntity.getProfilePicture());
        newEntity.setContent(oldEntity.getContent());

        newEntity = animalRepository.save(newEntity);
        return mapper.animalToAnimalDetailDto(newEntity);
    }

    @Override
    public AnimalDetailDto findById(UUID id) throws NotFoundException {
        LOGGER.trace("findById({}) ", id);

        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal with id " + id + " does not exist.")
        );

        return mapper.animalToAnimalDetailDto(animal);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        LOGGER.trace("delete({})", id);

        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );
        String contentId = animal.getProfilePicture().getContentId();

        animalRepository.delete(animal);
        java.io.File[] matchingFiles = Arrays.stream(new java.io.File("files").listFiles())
            .filter(file -> file.isFile() && file.getName().contains(contentId))
            .toArray(java.io.File[]::new);

        for (java.io.File f :
            matchingFiles) {
            f.delete();
        }
    }

    @Override
    public FileDetailDto getProfileImage(UUID id) throws NotFoundException {
        LOGGER.trace("getProfileImage({})", id);
        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        Optional.ofNullable(animal.getProfilePicture()).orElseThrow(
            () -> new NotFoundException("Animal does not have a profile image.")
        );

        return fileService.findById(animal.getProfilePicture().getId());
    }

    @Override
    public FileDto setProfileImage(UUID id, CreateFileDto createFileDto) throws ConflictException, ValidationException, IOException, NotFoundException {
        LOGGER.trace("setProfileImage({},{})", id, createFileDto);
        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        if (animal.getProfilePicture() != null) {
            throw new ConflictException("Profile Image already set.", Collections.singletonList(""));
        }

        var createdFile = fileService.create(createFileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        animal.setProfilePicture(file);
        animalRepository.save(animal);

        return createdFile;
    }

    @Override
    public FileDto updateProfileImage(UUID id, CreateFileDto fileDto) throws ValidationException, IOException, NotFoundException {

        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        UUID oldProfilePictureId = animal.getProfilePicture().getId();

        var createdFile = fileService.create(fileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        animal.setProfilePicture(file);
        animalRepository.save(animal);

        fileService.delete(oldProfilePictureId);
        return createdFile;
    }

    @Override
    public List<FileDto> getContents(UUID id) throws NotFoundException {
        LOGGER.trace("getContents({})", id);
        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        Hibernate.initialize(animal.getContent());

        return animal.getContent().stream().map(fileMapper::fileToFileDto).toList();
    }

    @Override
    public FileDetailDto getContent(UUID id, UUID fileId) throws NotFoundException {
        LOGGER.trace("getContent({},{})", id, fileId);
        if (!animalRepository.existsById(id)) {
            throw new NotFoundException("Animal does not exist.");
        }
        // TODO check if is image

        var user = userService.getCurrentUser();

        if (this.userService.isSubscribed(id, user.getId()) == null) {
            if (user.getShelter() != null && user.getShelter().getId() == findById(id).getShelter().getId()) {
                return fileService.findById(fileId);
            } else {
                try {
                    var fileDto = fileService.findById(fileId);

                /*if (fileDto.getContentMimeType().toLowerCase().startsWith("video")) {
                    throw new NotFoundException("Videos not shown when user not subscribed");
                };
                 */

                    return ImageBlur.blur(fileDto);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            return fileService.findById(fileId);
        }
    }

    @Override
    public FileDto addContent(UUID id, CreateFileDto fileDto) throws ValidationException, IOException, NotFoundException {
        LOGGER.trace("addContent({},{})", id, fileDto);
        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        Hibernate.initialize(animal.getContent());

        var createdFile = fileService.create(fileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        if (animal.getContent() == null) {
            animal.setContent(new ArrayList<>(Collections.singletonList(file)));
        } else {
            animal.getContent().add(file);
        }
        animalRepository.save(animal);

        return createdFile;
    }

    @Override
    public void deleteContent(UUID id, UUID fileId) throws NotFoundException {
        LOGGER.trace("deleteContent({},{})", id, fileId);
        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        Hibernate.initialize(animal.getContent());
        animal.getContent().removeIf(f -> f.getId().equals(fileId));
        fileService.delete(fileId);
    }

}
