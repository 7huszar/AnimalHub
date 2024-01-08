package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepr.groupphase.backend.entity.SubscriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionKey> {

    List<Subscription> findAllByUserId(UUID userId);


}
