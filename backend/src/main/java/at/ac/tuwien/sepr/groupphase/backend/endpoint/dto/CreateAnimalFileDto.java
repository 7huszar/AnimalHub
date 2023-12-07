package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnimalFileDto {

    private UUID animalId;

    private String name;

    private String summary;

    private MultipartFile file;

    @Override
    public String toString() {
        return "FileDetailDto{"
            + ", name='" + name + '\''
            + ", summary='" + summary + '\''
            + ", file='" + file + '\''
            + '}';
    }
}
