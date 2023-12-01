package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CreateShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface ShelterService {

    // TODO doku
    List<ShelterDetailDto> findAll();


    // TODO doku
    ShelterDetailDto create(CreateShelterDto shelterDto) throws ValidationException;

    // TODO doku
    ShelterDetailDto update(ShelterDetailDto shelterDto)throws ValidationException;

    // TODO doku

    ShelterDetailDto findById(UUID id);

}
