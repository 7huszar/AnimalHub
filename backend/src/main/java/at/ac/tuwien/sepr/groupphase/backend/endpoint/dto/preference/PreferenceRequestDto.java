package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.preference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PreferenceRequestDto {
    private UUID animalId;
    private UUID userId;
    private boolean like;
}
