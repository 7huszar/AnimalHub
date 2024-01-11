package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
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
public class CreateSubscriptionDto {

    private AnimalDetailDto animal;

    private UserDetailDto user;

    private String tier;

    private String subscriptionId;

    @Override
    public String toString() {
        return "CreateSubscriptionDto{"
            + "animal='" + animal + '\''
            + ", user=" + user
            + ", tier='" + tier + '\''
            + ", subscriptionId='" + subscriptionId + '\''
            + '}';
    }
}
