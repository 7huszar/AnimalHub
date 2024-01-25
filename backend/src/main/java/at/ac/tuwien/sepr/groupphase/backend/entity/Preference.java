package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PREFERENCE")
@IdClass(PreferenceId.class)
public class Preference {

    @Id
    @ManyToOne
    private ApplicationUser user;

    @Id
    @Column(nullable = false)
    private Species species;

    @Column(nullable = false)
    private double speciesFactor;

    @ElementCollection
    @Column(nullable = false)
    private Map<String, Double> traitPointMap;

}
