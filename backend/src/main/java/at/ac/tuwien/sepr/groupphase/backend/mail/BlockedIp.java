package at.ac.tuwien.sepr.groupphase.backend.mail;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class BlockedIp implements Blockable {
    private final int max;
    private final Timestamp timestamp;
    private final String ip;
    private int cnt = 1;
    private int timout;
    private final int base;

    public BlockedIp(String ip, int base, int attempts) {
        this.max = attempts;
        this.base = base;
        this.timout = this.base;
        this.ip = ip;
        timestamp = Timestamp.valueOf(LocalDateTime.now());
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return TimeUnit.MICROSECONDS
            .convert(timout + timestamp.getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (timestamp.getTime() - o.getDelay(TimeUnit.MICROSECONDS));
    }

    @Override
    public boolean decrement() {
        timout += this.base;
        return Math.max(--cnt, 0) == 0;
    }

    @Override
    public void increment() {
        cnt = Math.min(++cnt, max);
    }

    @Override
    public boolean hasTimeout() {
        return cnt == max;
    }

    @Override
    public String getKey() {
        return ip;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockedIp)) {
            return false;
        }
        return ((BlockedIp) o).ip.equals(this.ip);
    }

    @Override
    public String toString() {
        return "StampedEmail{email=" + ip + ", stamp=" + timestamp.toString() + "}";
    }

    @Override
    public long remainingTimout() {
        return this.getDelay(TimeUnit.MICROSECONDS);
    }
}
