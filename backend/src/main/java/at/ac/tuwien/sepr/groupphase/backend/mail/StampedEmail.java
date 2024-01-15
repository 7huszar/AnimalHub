package at.ac.tuwien.sepr.groupphase.backend.mail;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
public class StampedEmail implements Blockable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String email;
    private final Timestamp timestamp;

    StampedEmail(String email) {
        this.email = email;
        timestamp = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return TimeUnit.MICROSECONDS
            .convert(60_000 + timestamp.getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (timestamp.getTime() - o.getDelay(TimeUnit.MICROSECONDS));
    }

    @Override
    public boolean decrement() {
        return false;
    }

    @Override
    public void increment() {}

    @Override
    public boolean hasTimeout() {
        return true;
    }

    @Override
    public String getKey() {
        return email;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StampedEmail)) {
            return false;
        }
        return ((StampedEmail) o).email.equals(this.email);
    }

    @Override
    public String toString() {
        return "StampedEmail{email=" + email + ", stamp=" + timestamp.toString() + "}";
    }

    Override
    public long remainingTimout() {
        return this.getDelay(TimeUnit.MICROSECONDS);
    }
}
