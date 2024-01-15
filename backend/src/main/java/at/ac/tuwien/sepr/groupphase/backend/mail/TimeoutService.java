package at.ac.tuwien.sepr.groupphase.backend.mail;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

@RequiredArgsConstructor
@Repository
public class TimeoutService {
    private final Map<String, Blockable> blocked = new HashMap<>();
    private final DelayQueue<Blockable> queue = new DelayQueue<>();
    private final TimeoutCleaner cleaner;

    public void addEntity(Blockable entity) {
        if (this.blocked.containsKey(entity.getKey())) {
            this.blocked.get(entity.getKey()).increment();
        } else {
            this.blocked.put(entity.getKey(), entity);
            this.queue.add(entity);
        }
    }

    public boolean hasTimeout(String blocked) {
        if (this.blocked.containsKey(blocked)) {
            return this.blocked.get(blocked).hasTimeout();
        }
        return false;
    }

    public long remainingTimout(String blocked) {
        if (this.blocked.containsKey(blocked)) {
            return this.blocked.get(blocked).remainingTimout();
        } else return 0;
    }

    @PostConstruct
    public void startCleaner() {
        cleaner.cleanBlockedMails(queue, blocked);
    }

}
