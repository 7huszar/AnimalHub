package at.ac.tuwien.sepr.groupphase.backend.mail;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;

@RequiredArgsConstructor
@Repository
public class MailTimeout {
    private final Set<String> emails = new HashSet<>();
    private final DelayQueue<StampedEmail> blocked = new DelayQueue<>();
    private final TimeoutCleaner cleaner;

    public void addEmail(String email) {
        emails.add(email);
        blocked.add(new StampedEmail(email));
    }

    public boolean hasTimeout(String email) {
        return emails.contains(email);
    }

    @PostConstruct
    public void startCleaner() {
        cleaner.cleanBlockedMails(blocked, emails);
    }

}
