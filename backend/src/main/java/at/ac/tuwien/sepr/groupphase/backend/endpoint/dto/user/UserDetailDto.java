package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

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

    @NotNull(message = "Email must not be null")
    private String email;

    @Override
    public String toString() {
        return "ShelterDetailDto{"
            + ", name='" + email + '\''
            + '}';
    }
}

