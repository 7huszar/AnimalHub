package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Subscription {

    @EmbeddedId
    private SubscriptionKey key;

    @ManyToOne
    @MapsId("animalId")
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")

    private ApplicationUser user;

    private int tier;

    private String subscriptionId;


    @Override
    public String toString() {
        return "Shelter{"
            + "animal=" + animal
            + ", tier='" + tier + '\''
            + ", user=" + user
            + ", subscriptionId='" + subscriptionId + '\''
            + '}';
    }

}
