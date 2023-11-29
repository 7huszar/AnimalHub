package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateAnimalDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AnimalRepository animalRepository;
    private final AnimalMapper mapper;

    @Override
    public List<Animal> findAll() {
        LOGGER.debug("Find all Animals");
        return animalRepository.findAll();
    }

    @Override
    public AnimalDetailDto create(CreateAnimalDto animalDto){
        LOGGER.debug("Create new Animal: " + animalDto);
        Animal entity = mapper.createAnimalDtoToAnimal(animalDto);
        animalRepository.save(entity);
        return mapper.animalToAnimalDetailDto(entity);
    }


}
