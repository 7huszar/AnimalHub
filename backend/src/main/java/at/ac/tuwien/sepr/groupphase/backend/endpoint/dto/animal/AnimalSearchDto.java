package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal;

import at.ac.tuwien.sepr.groupphase.backend.enums.Gender;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
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
public class AnimalSearchDto {
    private String name;

    private Gender gender;

    private Species species;

    private String race;

    private LocalDate bornBefore;

    private LocalDate bornAfter;

    private double heavierThan;

    private double lighterThan;

    private double tallerThan;

    private double smallerThan;

    @Override
    public String toString() {
        return "AnimalSearchDto{"
            + "name='" + name + '\''
            + ", gender=" + gender
            + ", species=" + species
            + ", race='" + race + '\''
            + ", bornBefore=" + bornBefore
            + ", bornAfter=" + bornAfter
            + ", heavierThan=" + heavierThan
            + ", lighterThan=" + lighterThan
            + ", tallerThan=" + tallerThan
            + ", smallerThan=" + smallerThan
            + '}';
    }
}




