package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalShelterMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.AnimalValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalRepository animalRepository;
    private final AnimalShelterMapper mapper;
    private final AnimalValidator validator;

    @Override
    public List<AnimalDetailDto> findAll() {
        LOGGER.debug("Find all Animals");

        return animalRepository.findAll().stream()
            .map(mapper::animalToAnimalDetailDto)
            .collect(Collectors.toList());
    }

    @Override
    public AnimalDetailDto create(CreateAnimalDto animalDto) throws ValidationException {
        LOGGER.debug("Create new Animal: " + animalDto);

        validator.validateForCreate(animalDto);
        Animal entity = mapper.createAnimalDtoToAnimal(animalDto);

        entity = animalRepository.save(entity);
        return mapper.animalToAnimalDetailDto(entity);
    }

    @Override
    public AnimalDetailDto update(AnimalDetailDto animalDto) throws ValidationException {
        LOGGER.debug("Update Animal with id: " + animalDto.getId() + ", with following data:" + animalDto);

        validator.validateForUpdate(animalDto);
        Animal entity = mapper.animalDetailDtoToAnimal(animalDto);
        entity = animalRepository.save(entity);
        return mapper.animalToAnimalDetailDto(entity);
    }

    @Override
    public AnimalDetailDto findById(UUID id) {
        LOGGER.debug("Get Animal with id: " + id);

        var animal = animalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Animal does not exist.")
        );

        return mapper.animalToAnimalDetailDto(animal);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.debug("Delete Animal with id: " + id);

        var shelter = animalRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Shelter does not exist.")
            );

        animalRepository.delete(shelter);
    }

}
