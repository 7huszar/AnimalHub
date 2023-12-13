package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ANIMAL")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToOne(cascade = CascadeType.REMOVE)
    private File profilePicture;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Species species;

    @Column(nullable = false, length = 100)
    private String race;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Column
    private double height;

    @Column
    private double weight;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(targetEntity = File.class, cascade = CascadeType.REMOVE)
    private List<File> content;

    @Column(name = "created_on")
    @CreationTimestamp
    private Instant createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Animal animal = (Animal) o;
        return Double.compare(height, animal.height) == 0 && Double.compare(weight, animal.weight) == 0 && Objects.equals(id, animal.id) && Objects.equals(name, animal.name) && species == animal.species
            && Objects.equals(race, animal.race) && Objects.equals(birthDate, animal.birthDate) && Objects.equals(createdOn, animal.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, species, race, birthDate, height, weight, createdOn);
    }

    @Override
    public String toString() {
        return "Animal{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", species=" + species
            + ", race='" + race + '\''
            + ", birthDate=" + birthDate
            + ", height=" + height
            + ", weight=" + weight
            + ", createdOn=" + createdOn
            + '}';
    }
}
