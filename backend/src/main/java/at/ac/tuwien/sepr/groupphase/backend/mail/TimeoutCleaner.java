package at.ac.tuwien.sepr.groupphase.backend.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.DelayQueue;

@EnableAsync
@Component
public class TimeoutCleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Async
    public void cleanBlockedMails(DelayQueue<Blockable> queue, Map<String, Blockable> blocked) {
        boolean run = true;
        while (run) {
            try {
                Blockable expired = queue.take();
                if (expired.decrement()) {
                    blocked.remove(expired.getKey());
                } else  {
                    queue.add(expired);
                }
            } catch (InterruptedException e) {
                LOGGER.trace("Thread of cleanBlockedMails() shutdown: " + e.getMessage());
                run = false;
            }
        }

    }
}