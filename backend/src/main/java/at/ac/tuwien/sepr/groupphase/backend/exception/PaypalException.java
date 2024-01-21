package at.ac.tuwien.sepr.groupphase.backend.exception;

import lombok.Getter;
import org.springframework.web.reactive.function.client.ClientResponse;

/**
 * Exception handling negative responses from PayPal.
 */
@Getter
public class PaypalException extends Exception {
    private final ClientResponse response;

    /**
     * Sets message to 'PayPal action failed' and saves the response from PayPal.
     *
     * @param response the response from PayPal
     */
    public PaypalException(ClientResponse response) {
        super("PayPal action failed");
        this.response = response;
    }
}
