package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDetailDto {

    private double id;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Species must not be null")
    private String species;

    private String race;

    @NotNull(message = "Birth Date must not be null")
    private LocalDate birthDate;

    @NotNull(message = "Height must not be null")
    private double height;

    @NotNull(message = "Weight must not be null")
    private double weight;

    @Override
    public String toString() {
        return "CreateAnimalDto{"
            + "id='" + id + '\''
            + "name='" + name + '\''
            + ", species='" + species + '\''
            + ", birthDate='" + birthDate + '\''
            + ", height='" + height + '\''
            + ", weight='" + weight + '\''
            + '}';
    }
}
