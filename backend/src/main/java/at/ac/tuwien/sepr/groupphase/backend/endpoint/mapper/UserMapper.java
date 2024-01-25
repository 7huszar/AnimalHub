package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.CreateSubscriptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.subscription.SubscriptionDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(uses = AnimalShelterMapper.class)
public interface UserMapper {

    UserDto applicationUserToUserDto(ApplicationUser user);

    UserDto userDetailDtoToUserDto(UserDetailDto user);

    UserDetailDto applicationUserToUserDetailDto(ApplicationUser user);

    ApplicationUser userDetailDtoUserToApplicationUser(UserDetailDto user);


    Subscription subscriptionDetailDtoToSubscription(SubscriptionDetailDto subscriptionDetailDto);

    Subscription createSubscriptionDtoToSubscription(CreateSubscriptionDto subscriptionDetailDto);


    SubscriptionDetailDto subscriptionToSubscriptionDetailDto(Subscription subscription);

}
