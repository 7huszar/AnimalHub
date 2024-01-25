package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.DonationType;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shelter shelter;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApplicationUser user;

    @Column(scale = 2)
    private BigDecimal amount;

    private LocalDateTime stamp;

    private boolean payed;

    private DonationType type;

    private UUID receiver;
}
