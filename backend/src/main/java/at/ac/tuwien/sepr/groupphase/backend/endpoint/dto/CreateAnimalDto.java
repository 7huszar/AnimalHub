package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

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
            + "name='" + name + '\''
            + ", species='" + species + '\''
            + ", birthDate='" + birthDate + '\''
            + ", height='" + height + '\''
            + ", weight='" + weight + '\''
            + '}';
    }


}
