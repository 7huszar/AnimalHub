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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LIKEDDISLIKEDANIMAL")
@IdClass(LikedDislikedAnimalId.class)
public class LikedDislikedAnimal {

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApplicationUser user;

    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private Animal animal;

    @Column(nullable = false)
    private boolean liked;
}
