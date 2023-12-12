package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShelterDto {

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Address must not be null")
    private String address;

    @NotNull(message = "Phone Number must not be null")
    private String phoneNumber;

    @NotNull(message = "Email must not be null")
    private String email;

    private Instant createdOn;

    @Override
    public String toString() {
        return "CreateShelterDto{"
            + ", name='" + name + '\''
            + ", address=" + address
            + ", phoneNumber='" + phoneNumber + '\''
            + ", email=" + email + '\''
            + ", createdOn=" + createdOn + '\''
            + '}';
    }


}
