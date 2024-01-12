package at.ac.tuwien.sepr.groupphase.backend.mail;

import java.util.concurrent.Delayed;

public interface Blockable extends Delayed {

    boolean decrement();

    void increment();

    boolean hasTimeout();

    String getKey();
}
