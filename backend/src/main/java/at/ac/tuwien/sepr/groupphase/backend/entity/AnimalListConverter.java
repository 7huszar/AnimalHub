package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.AnimalMapper;
import at.ac.tuwien.sepr.groupphase.backend.service.AnimalService;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

import static java.util.Collections.*;

@jakarta.persistence.Converter
@RequiredArgsConstructor
public class AnimalListConverter implements AttributeConverter<List<Animal>, String> {
    private static final String SPLIT_CHAR = ";";
    private final AnimalService animalService;
    private final AnimalMapper animalMapper;

    @Override
    public String convertToDatabaseColumn(List<Animal> animalList) {

        if(animalList.isEmpty()) return "";

        List<String> animalListWithId = emptyList();

        for (Animal animal : animalList) {
            animalListWithId.add(animal.getId().toString());
        }

        return String.join(SPLIT_CHAR, animalListWithId);
    }

    @Override
    public List<Animal> convertToEntityAttribute(String string) {

        if(string == null) return emptyList();

        List<String> idString = Arrays.asList(string.split(SPLIT_CHAR));

        List<Animal> animalList = emptyList();

        for (String id: idString
             ) {
            animalList.add(animalMapper.animalDetailDtoToAnimal(animalService.findById(UUID.fromString(id))));
        }

        return animalList;
    }
}