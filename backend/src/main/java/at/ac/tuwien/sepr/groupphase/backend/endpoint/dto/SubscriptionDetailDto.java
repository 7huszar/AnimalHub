package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.SubscriptionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDetailDto {

    private SubscriptionKey id;

    private Animal animal;

    private ApplicationUser user;

    private int tier;

    private int subscriptionId;

    @Override
    public String toString() {
        return "SubscriptionDetailDto{"
            + "id=" + id
            + ", subscriptionId='" + subscriptionId + '\''
            + ", tier=" + tier
            + ", animal='" + animal + '\''
            + ", user=" + user
            + '}';
    }
}
