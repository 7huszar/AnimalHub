package at.ac.tuwien.sepr.groupphase.backend.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.concurrent.DelayQueue;

@EnableAsync
@Component
public class TimeoutCleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Async
    public void cleanBlockedMails(DelayQueue<StampedEmail> blocked, Set<String> emails) {
        boolean run = true;
        while (run) {
            try {
                emails.remove(blocked.take().getEmail());
                LOGGER.debug("removed something");
            } catch (InterruptedException e) {
                LOGGER.trace("Thread of cleanBlockedMails() shutdown:\n" + e.getMessage());
                run = false;
            }
        }

    }
}