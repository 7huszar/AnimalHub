package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
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

    private AnimalDetailDto animal;

    private UserDetailDto user;

    private int tier;

    private String subscriptionId;

    private Boolean cancelled;

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
