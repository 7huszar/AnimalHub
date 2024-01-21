package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.Gender;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDetailDto {

    private UUID id;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Gender must not be null")
    private Gender gender;

    private String germanDescription;

    private String englishDescription;

    @NotNull(message = "Species must not be null")
    private Species species;

    @NotNull(message = "English Race must not be null")
    private String englishRace;

    @NotNull(message = "German Race must not be null")
    private String germanRace;

    @NotNull(message = "Birth Date must not be null")
    private LocalDate birthDate;

    @NotNull(message = "Shelter must not be null")
    private ShelterDto shelter;

    private double height;

    private double weight;

    private Instant createdOn;

    @Override
    public String toString() {
        return "AnimalDetailDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", gender=" + gender
            + ", germanDescription='" + germanDescription + '\''
            + ", englishDescription='" + englishDescription + '\''
            + ", species=" + species
            + ", englishRace='" + englishRace + '\''
            + ", germanRace='" + germanRace + '\''
            + ", birthDate=" + birthDate
            + ", shelter=" + shelter.getName()
            + ", height=" + height
            + ", weight=" + weight
            + ", createdOn=" + createdOn
            + '}';
    }
}
