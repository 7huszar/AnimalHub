package at.ac.tuwien.sepr.groupphase.backend.scheduled;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@AllArgsConstructor
@EnableScheduling
@Component
public class SubscriptionCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SubscriptionRepository repository;
    private final UserEndpoint endpoint;

    @Scheduled(cron = "@monthly")
    public void cleanSubscriptions() {
        LOGGER.trace("cleanSubscriptions()");


        repository.findAll().forEach((subscription) -> {
            try {
                endpoint.checkIfSubscriptionActive(subscription.getSubscriptionId(), subscription.getAnimal().getShelter().getId());
            } catch (NotFoundException e) {
                // already handled
            }
        });
    }
}
