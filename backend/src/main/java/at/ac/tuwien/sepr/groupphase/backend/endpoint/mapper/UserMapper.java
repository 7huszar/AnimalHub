package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(uses = AnimalShelterMapper.class)
public interface UserMapper {
    UserDetailDto applicationUserToUserDetailDto(ApplicationUser user);

    Subscription subscriptionDetailDtoToSubscription(SubscriptionDetailDto subscriptionDetailDto);

    SubscriptionDetailDto subscriptionToSubscriptionDetailDto(Subscription subscription);


}
