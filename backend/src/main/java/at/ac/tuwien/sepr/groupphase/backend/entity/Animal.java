package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.Gender;
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
    @Column(name = "animal_id")
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = true, length = 500)
    private String englishDescription;

    @Column(nullable = true, length = 500)
    private String germanDescription;

    @OneToOne(cascade = CascadeType.REMOVE)
    private File profilePicture;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Species species;

    @Column(nullable = false, length = 255)
    private String germanRace;

    @Column(nullable = false, length = 255)
    private String englishRace;

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

        if (Double.compare(height, animal.height) != 0) {
            return false;
        }
        if (Double.compare(weight, animal.weight) != 0) {
            return false;
        }
        if (!id.equals(animal.id)) {
            return false;
        }
        if (!name.equals(animal.name)) {
            return false;
        }
        if (gender != animal.gender) {
            return false;
        }
        if (!Objects.equals(englishDescription, animal.englishDescription)) {
            return false;
        }
        if (!Objects.equals(germanDescription, animal.germanDescription)) {
            return false;
        }
        if (!profilePicture.equals(animal.profilePicture)) {
            return false;
        }
        if (species != animal.species) {
            return false;
        }
        if (!germanRace.equals(animal.germanRace)) {
            return false;
        }
        if (!englishRace.equals(animal.englishRace)) {
            return false;
        }
        if (!birthDate.equals(animal.birthDate)) {
            return false;
        }
        if (!shelter.equals(animal.shelter)) {
            return false;
        }
        if (!Objects.equals(content, animal.content)) {
            return false;
        }
        return createdOn.equals(animal.createdOn);
    }

    @Override
    public int hashCode() {
        int result;
        result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (englishDescription != null ? englishDescription.hashCode() : 0);
        result = 31 * result + (germanDescription != null ? germanDescription.hashCode() : 0);
        result = 31 * result + profilePicture.hashCode();
        result = 31 * result + species.hashCode();
        result = 31 * result + germanRace.hashCode();
        result = 31 * result + englishRace.hashCode();
        result = 31 * result + birthDate.hashCode();
        long temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + shelter.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + createdOn.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Animal{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", gender=" + gender
            + ", englishDescription='" + englishDescription + '\''
            + ", germanDescription='" + germanDescription + '\''
            + ", profilePicture=" + profilePicture
            + ", species=" + species
            + ", germanRace='" + germanRace + '\''
            + ", englishRace='" + englishRace + '\''
            + ", birthDate=" + birthDate
            + ", height=" + height
            + ", weight=" + weight
            + ", shelter=" + shelter.getName()
            + ", content=" + content
            + ", createdOn=" + createdOn
            + '}';
    }
}
