package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.AnimalValidator;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.ShelterValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShelterRepository shelterRepository;
    private final ShelterMapper mapper;
    private final ShelterValidator validator;

    @Override
    public List<ShelterDetailDto> findAll() {
        LOGGER.debug("Find all Shelters");

        return shelterRepository.findAll().stream().map(mapper::shelterToShelterDetailDto).toList();
    }

    @Override
    public ShelterDetailDto create(CreateShelterDto shelterDto) throws ValidationException {
        LOGGER.debug("Create new Shelter: " + shelterDto);

        validator.validateForCreate(shelterDto);
        Shelter entity = mapper.createShelterDtoToShelter(shelterDto);
        shelterRepository.save(entity);
        return mapper.shelterToShelterDetailDto(entity);
    }

    @Override
    public ShelterDetailDto update(ShelterDetailDto shelterDto) throws ValidationException {
        LOGGER.debug("Update Shelter with id: " + shelterDto.getId() + ", with following data:" + shelterDto);

        validator.validateForUpdate(shelterDto);
        shelterRepository.deleteById(shelterDto.getId());
        Shelter entity = mapper.shelterDetailDtoToShelter(shelterDto);
        shelterRepository.save(entity);
        return mapper.shelterToShelterDetailDto(entity);
    }

    @Override
    public ShelterDetailDto findById(UUID id) {
        LOGGER.debug("Get Shelter with id: " + id);

        var shelter = shelterRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Shelter does not exist.")
        );

        return mapper.shelterToShelterDetailDto(shelter);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.debug("Delete Shelter with id: " + id);

        var shelter = shelterRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Shelter does not exist.")
            );

        shelterRepository.delete(shelter);
    }

}
