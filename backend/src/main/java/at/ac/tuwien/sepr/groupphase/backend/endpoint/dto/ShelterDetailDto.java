package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShelterDetailDto {

    private UUID id;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Address must not be null")
    private String address;

    @NotNull(message = "Phone Number must not be null")
    private String phoneNumber;

    @NotNull(message = "Email must not be null")
    private String email;

    private Set<AnimalDetailDto> animals;

    private Instant createdOn;

    @Override
    public String toString() {
        return "ShelterDetailDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", address=" + address
            + ", phoneNumber='" + phoneNumber + '\''
            + ", email=" + email
            + ", animals=" + animals
            + ", createdOn=" + createdOn
            + '}';
    }
}
