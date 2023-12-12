package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.fileDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.InputStreamResource;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDetailDto {

    private UUID id;

    private String name;

    private String summary;

    private Instant createdOn;

    private String contentMimeType;

    private long contentLength;

    private InputStreamResource file;

    @Override
    public String toString() {
        return "FileDetailDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", summary='" + summary + '\''
            + ", created='" + createdOn + '\''
            + ", contentMimeType='" + contentMimeType + '\''
            + '}';
    }
}
