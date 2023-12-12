package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnimalDto {

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Species must not be null")
    private Species species;

    @NotNull(message = "Race must not be null")
    private String race;

    @NotNull(message = "Birth Date must not be null")
    private LocalDate birthDate;

    @NotNull(message = "Shelter must not be null")
    private ShelterDto shelter;

    private double height;

    private double weight;

    @Override
    public String toString() {
        return "CreateAnimalDto{"
            + "name='" + name + '\''
            + ", species='" + species + '\''
            + ", race='" + race + '\''
            + ", birthDate='" + birthDate + '\''
            + ", height='" + height + '\''
            + ", weight='" + weight + '\''
            + '}';
    }
}
