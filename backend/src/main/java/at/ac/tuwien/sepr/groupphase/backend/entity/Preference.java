package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PREFERENCE")
public class Preference {

    @Id
    @ManyToOne
    private ApplicationUser user;

    @Id
    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private long speciesFactor;

    @ElementCollection
    @Column(nullable = false)
    private Map<String, Integer> traitPoints;
}
