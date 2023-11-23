package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 60)
    private String kind;

    @Column(nullable = false, length = 60)
    private String race;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Column
    private double height;

    @Column
    private double weight;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Double.compare(height, animal.height) == 0 && Double.compare(weight, animal.weight) == 0 && Objects.equals(id, animal.id) && Objects.equals(name, animal.name) && Objects.equals(kind, animal.kind) && Objects.equals(race, animal.race) && Objects.equals(birthDate, animal.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, kind, race, birthDate, height, weight);
    }

    @Override
    public String toString() {
        return "Animal{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", kind='" + kind + '\'' +
            ", race='" + race + '\'' +
            ", birthDate=" + birthDate +
            ", height=" + height +
            ", weight=" + weight +
            '}';
    }
}
