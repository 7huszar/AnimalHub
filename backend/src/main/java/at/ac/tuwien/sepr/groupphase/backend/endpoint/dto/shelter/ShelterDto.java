package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelterDto {

    private UUID id;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Address must not be null")
    private String address;

    @NotNull(message = "Phone Number must not be null")
    private String phoneNumber;

    @NotNull(message = "Email must not be null")
    private String email;

    private String donationId;

    private String subscriptionId;

    private Instant createdOn;

    private String paypalClientId;

    private String paypalSecret;

    @Override
    public String toString() {
        return "ShelterDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", address='" + address + '\''
            + ", phoneNumber='" + phoneNumber + '\''
            + ", email='" + email + '\''
            + ", donation=" + donationId + '\''
            + ", donation=" + donationId + '\''
            + ", createdOn=" + createdOn
            + '}';
    }
}
