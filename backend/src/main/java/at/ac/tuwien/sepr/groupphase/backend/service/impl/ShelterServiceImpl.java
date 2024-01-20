package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.CreateFileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.FileService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.conflict.UserConflict;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.ShelterValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
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
    private final UserRepository userRepository;
    private final AnimalShelterMapper mapper;
    private final UserMapper userMapper;
    private final ShelterValidator validator;
    private final UserService userService;
    private final UserConflict conflict;


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
    public ShelterDto update(ShelterDto shelterDto) throws ValidationException, NotFoundException {
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
    public ShelterDetailDto findById(UUID id) throws NotFoundException {
        LOGGER.debug("Get Shelter with id: " + id);

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        Hibernate.initialize(shelter.getAnimals());

        return mapper.shelterToShelterDetailDto(shelter);
    }


    @Override
    public void delete(UUID id) throws NotFoundException {
        LOGGER.debug("Delete Shelter with id: " + id);

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        shelterRepository.delete(shelter);
    }

    @Override
    public FileDetailDto getProfileImage(UUID id) throws NotFoundException {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        Optional.ofNullable(shelter.getProfilePicture()).orElseThrow(
            () -> new NotFoundException("Animal does not have a profile image.")
        );

        return fileService.findById(shelter.getProfilePicture().getId());
    }

    @Override
    public FileDto setProfileImage(UUID id, CreateFileDto createFileDto) throws ConflictException, ValidationException, IOException, NotFoundException {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        if (shelter.getProfilePicture() != null) {
            throw new ConflictException("Profile Image already set.", Collections.singletonList(""));
        }

        var createdFile = fileService.create(createFileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        shelter.setProfilePicture(file);
        shelterRepository.save(shelter);

        return createdFile;
    }

    @Override
    public FileDto updateProfileImage(UUID id, CreateFileDto createFileDto) throws ValidationException, IOException, NotFoundException {

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        UUID oldProfilePictureId = shelter.getProfilePicture().getId();

        var createdFile = fileService.create(createFileDto);
        var file = fileMapper.fileDtoToFile(createdFile);

        shelter.setProfilePicture(file);
        shelterRepository.save(shelter);

        fileService.delete(oldProfilePictureId);

        return createdFile;
    }

    @Override
    public void assignShelterToUser(UUID shelterId, UserDetailDto user) throws NotFoundException, NotAuthorizedException {

        LOGGER.debug("assign Shelter({}) to user({})", shelterId, user);
        conflict.conflictForShelterUserUpdate(shelterId, user);

        Optional<Shelter> shelter = shelterRepository.findById(shelterId);
        Shelter foundShelter;
        if (shelter.isPresent()) {
            foundShelter = shelter.get();
            foundShelter.setUser(userMapper.userDetailDtoUserToApplicationUser(user));
            user.setShelter(mapper.shelterToShelterDto(foundShelter));
        } else {
            throw new NotFoundException("Could not find the Paypal ID");
        }
        ApplicationUser applicationUser = userService.findApplicationUserByEmail(user.getEmail());
        applicationUser.setShelter(foundShelter);

        shelterRepository.save(foundShelter);
        userRepository.save(applicationUser);

    }

    @Override
    public ShelterDetailDto isAssignedTo(UserDetailDto user) {
        LOGGER.debug("looking for assigned Shelter of User({})", user);

        var shelter = shelterRepository.findByUser(userMapper.userDetailDtoUserToApplicationUser(user));
        if (shelter == null) {
            return new ShelterDetailDto();
        }

        return mapper.shelterToShelterDetailDto(shelterRepository.findByUser(userMapper.userDetailDtoUserToApplicationUser(user)));
    }


    @Override
    public void unassignShelter(UUID shelterId, UserDetailDto user) throws NotFoundException, ConflictException {

        LOGGER.debug("unassign Shelter({}) from user({})", shelterId, user);

        Optional<Shelter> shelter = shelterRepository.findById(shelterId);
        Shelter foundShelter;
        if (shelter.isPresent()) {
            foundShelter = shelter.get();
            foundShelter.setUser(null);
        } else {
            throw new NotFoundException("Could not find the Paypal ID");
        }
        ApplicationUser applicationUser = userService.findApplicationUserByEmail(user.getEmail());
        applicationUser.setShelter(null);

        shelterRepository.save(foundShelter);
        userRepository.save(applicationUser);

    }
}
