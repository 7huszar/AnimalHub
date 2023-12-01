package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 400)
    private String address;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @Email
    @Column(nullable = false, length = 100)
    private String email;

    @Convert(converter = AnimalListConverter.class)
    private List<Animal> animals;


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
        Shelter shelter = (Shelter) o;
        return Objects.equals(id, shelter.id) && Objects.equals(name, shelter.name) && Objects.equals(address, shelter.address) && Objects.equals(phoneNumber, shelter.phoneNumber) && Objects.equals(email, shelter.email) && Objects.equals(animals, shelter.animals) &&
            Objects.equals(createdOn, shelter.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phoneNumber, email, animals, createdOn);
    }

    @Override
    public String toString() {
        return "Shelter{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address=" + address +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", email=" + email +
            ", animals=" + animals +
            ", createdOn=" + createdOn +
            '}';
    }
}
