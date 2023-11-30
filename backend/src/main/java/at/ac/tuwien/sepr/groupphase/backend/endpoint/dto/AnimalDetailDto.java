package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDetailDto {

    private UUID id;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Species must not be null")
    private Species species;

    private String race;

    @NotNull(message = "Birth Date must not be null")
    private LocalDate birthDate;

    @NotNull(message = "Height must not be null")
    private double height;

    @NotNull(message = "Weight must not be null")
    private double weight;

    private Instant createdOn;

    @Override
    public String toString() {
        return "AnimalDetailDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", species=" + species +
            ", race='" + race + '\'' +
            ", birthDate=" + birthDate +
            ", height=" + height +
            ", weight=" + weight +
            ", createdOn=" + createdOn +
            '}';
    }
}
