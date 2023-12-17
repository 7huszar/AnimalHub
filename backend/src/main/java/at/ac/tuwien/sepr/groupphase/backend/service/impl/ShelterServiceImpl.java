package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.ShelterValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileService fileService;
    private final FileMapper fileMapper;
    private final ShelterRepository shelterRepository;
    private final AnimalShelterMapper mapper;
    private final ShelterValidator validator;

    /**
     * Search for users in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in user.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the users where the given fields match.
     */
    @Override
    public List<ShelterListDto> search(ShelterSearchDto searchParameters) {
        var shelters = shelterRepository.findByNameIgnoreCaseContaining(searchParameters.name());

        return shelters.stream()
            .map(mapper::shelterToShelterListDto).toList();
    }

    @Override
    public List<ShelterDto> findAll() {
        LOGGER.debug("Find all Shelters");

        return shelterRepository.findAll().stream()
            .map(mapper::shelterToShelterDto).toList();
    }

    @Override
    public ShelterDto create(CreateShelterDto shelterDto) throws ValidationException {
        LOGGER.debug("Create new Shelter: " + shelterDto);

        validator.validateForCreate(shelterDto);
        Shelter entity = mapper.createShelterDtoToShelter(shelterDto);
        entity = shelterRepository.save(entity);
        return mapper.shelterToShelterDto(entity);
    }

    @Override
    public ShelterDto update(ShelterDto shelterDto) throws ValidationException {
        LOGGER.debug("Update Shelter with id: " + shelterDto.getId() + ", with following data:" + shelterDto);

        validator.validateForUpdate(shelterDto);
        Shelter newEntity = mapper.shelterDtoToShelter(shelterDto);

        // Get old entity for the animal set
        Shelter oldEntity = shelterRepository.findById(newEntity.getId()).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        // Set the animal set for the new entity to avoid hibernate errors
        newEntity.setAnimals(oldEntity.getAnimals());
        newEntity.setProfilePicture(oldEntity.getProfilePicture());

        newEntity = shelterRepository.save(newEntity);
        return mapper.shelterToShelterDto(newEntity);
    }

    @Override
    public ShelterDetailDto findById(UUID id) {
        LOGGER.debug("Get Shelter with id: " + id);

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        Hibernate.initialize(shelter.getAnimals());

        return mapper.shelterToShelterDetailDto(shelter);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.debug("Delete Shelter with id: " + id);

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        shelterRepository.delete(shelter);
    }

    @Override
    public FileDetailDto getProfileImage(UUID id) {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        Optional.ofNullable(shelter.getProfilePicture()).orElseThrow(
            () -> new NotFoundException("Animal does not have a profile image.")
        );

        return fileService.findById(shelter.getProfilePicture().getId());
    }

    @Override
    public FileDto setProfileImage(UUID id, CreateFileDto createFileDto) throws ConflictException, ValidationException, IOException {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        if (shelter.getProfilePicture() != null) {
            throw new ConflictException("Profile Image already set.");
        }

        var createdFile = fileService.create(createFileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        shelter.setProfilePicture(file);
        shelterRepository.save(shelter);

        return createdFile;
    }

    @Override
    public FileDto updateProfileImage(UUID id, CreateFileDto fileDto) throws ValidationException, IOException {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        // TODO remove old image

        var createdFile = fileService.create(fileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        shelter.setProfilePicture(file);
        shelterRepository.save(shelter);

        return createdFile;
    }
}
