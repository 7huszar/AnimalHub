package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TraitType {
    private String type;
    private TraitPoints traitPoints;

}
