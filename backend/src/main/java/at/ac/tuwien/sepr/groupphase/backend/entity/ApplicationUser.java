package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "applicationuser")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(unique = true, length = 254)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime registrationStamp;


    @OneToOne
    @JoinColumn(name = "shelter_id", nullable = true)
    private Shelter shelter;

    /*@Override
    public String toString() {
        return "ApplicationUser{"
            + "id=" + id
            + ", email='" + email + '\''
            + ", password='" + password + '\''
            + ", role=" + role + '\''
            + ", shelter=" + shelter.getName() + '\''
            + '}';
    }*/
}
