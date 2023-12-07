package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SHELTER")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @OneToOne
    private File profilePicture;

    @Column(nullable = false, length = 400)
    private String address;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @Email
    @Column(nullable = false, length = 100)
    private String email;

    @OneToMany(mappedBy = "shelter")
    private Set<Animal> animals;


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
        return Objects.equals(id, shelter.id) && Objects.equals(name, shelter.name) && Objects.equals(address, shelter.address) && Objects.equals(phoneNumber, shelter.phoneNumber) && Objects.equals(email, shelter.email)
            && Objects.equals(animals, shelter.animals) && Objects.equals(createdOn, shelter.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phoneNumber, email, animals, createdOn);
    }

    @Override
    public String toString() {
        return "Shelter{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", address=" + address
            + ", phoneNumber='" + phoneNumber + '\''
            + ", email=" + email
            + ", animals=" + animals
            + ", createdOn=" + createdOn
            + '}';
    }
}
