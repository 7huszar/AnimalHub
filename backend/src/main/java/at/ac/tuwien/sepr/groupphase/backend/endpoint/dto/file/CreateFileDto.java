package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileDto {

    private String name;

    private String summary;

    private MultipartFile file;

    @Override
    public String toString() {
        return "CreateFileDto{"
            + "name='" + name + '\''
            + ", summary='" + summary + '\''
            + '}';
    }
}
