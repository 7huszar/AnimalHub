package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "donation")
public class Donation {

    @Id
    private String id;

    @ManyToOne
    private Shelter shelter;

    @ManyToOne
    private ApplicationUser user;

    private int amount;

    private LocalDateTime stamp;

    private boolean payed;
}
