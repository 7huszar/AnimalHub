package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {


    FileDetailDto fileToFileDetailDto(File file);

}

