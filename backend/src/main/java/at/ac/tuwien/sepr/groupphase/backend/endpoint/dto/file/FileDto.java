package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private UUID id;

    private String name;

    private String summary;

    private Instant createdOn;

    private String contentMimeType;

    @Override
    public String toString() {
        return "FileDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", summary='" + summary + '\''
            + ", createdOn=" + createdOn
            + '}';
    }
}
