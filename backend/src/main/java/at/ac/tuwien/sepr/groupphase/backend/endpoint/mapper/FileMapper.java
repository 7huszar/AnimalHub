package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.fileDtos.FileDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.fileDtos.FileDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {

    File fileDtoToFile(FileDto fileDto);

    FileDto fileToFileDto(File file);

    FileDetailDto fileToFileDetailDto(File file);

    FileDto fileDetailDtoToFileDto(FileDetailDto fileDetailDto);

}