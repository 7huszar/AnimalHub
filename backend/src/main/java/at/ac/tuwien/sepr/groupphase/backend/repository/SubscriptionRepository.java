package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepr.groupphase.backend.entity.SubscriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionKey> {

    List<Subscription> findAllByUserId(UUID userId);

    Subscription findBySubscriptionId(String subscriptionId);

    Subscription findByKey(SubscriptionKey id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Subscription SET cancelled = true WHERE subscriptionId = :subscriptionId")
    void setCancelled(@Param("subscriptionId") String subscriptionId);
}
