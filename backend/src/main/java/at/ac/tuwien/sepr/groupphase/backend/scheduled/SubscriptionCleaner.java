package at.ac.tuwien.sepr.groupphase.backend.scheduled;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.PaypalProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.enums.DonationType;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class SubscriptionCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SubscriptionRepository repository;
    private final DonationRepository donationRepository;
    private final UserEndpoint endpoint;
    private final PaypalProperties paypalProperties;

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

        final LocalDateTime date = LocalDateTime.now();
        repository.findAll().forEach((subscription -> {
            Donation donation = Donation.builder()
                .user(subscription.getUser())
                .amount(paypalProperties.getHarriesHardcodedAmount())
                .stamp(date)
                .payed(true)
                .receiver(subscription.getAnimal().getId())
                .id(subscription.getSubscriptionId() + LocalDate.now().getYear() + LocalDate.now().getMonth())
                .shelter(subscription.getAnimal().getShelter())
                .type(DonationType.SUBSCRIPTION)
                .build();

            donationRepository.save(donation);
        }));
    }
}
