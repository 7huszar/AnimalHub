package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Embeddable
@Getter
@Setter
public class TraitPoints {
    private Map<String, Long> traitPoints;

}
