package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {

    private UUID id;

    @NotNull(message = "Email must not be null")
    private String email;

    private UserRole role;

    private ShelterDto shelter;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserDetailDto{"
            + "id='" + id + '\''
            + ", email='" + email + '\''
            + ", role='" + role + '\''
            + ", secret='");
        sb.append("'}");
        return sb.toString();
    }
}

