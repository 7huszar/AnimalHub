package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.ShelterValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShelterRepository shelterRepository;
    private final AnimalShelterMapper mapper;
    private final ShelterValidator validator;
    private final AnimalService animalService;

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
        Shelter entity = mapper.shelterDtoToShelter(shelterDto);
        entity = shelterRepository.save(entity);
        return mapper.shelterToShelterDto(entity);
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

        var shelter = shelterRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Shelter does not exist.")
            );

        if (shelter.getAnimals() != null) {
            if (!shelter.getAnimals().isEmpty()) {
                Set<Animal> animalsToDelete = shelter.getAnimals();

                for (Animal animal : animalsToDelete) {
                    animalService.delete(animal.getId());
                }
            }
        }
        shelterRepository.delete(shelter);
    }
}
