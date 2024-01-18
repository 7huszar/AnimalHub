package at.ac.tuwien.sepr.groupphase.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {
    private String clientId;
    private String clientSecret;
    private String sandbox;
    private String auth;
    private String placeOrder;
    private String captureOrder1;
    private String captureOrder2;

    public String captureOrder(String id) {
        return getCaptureOrder1() + id + getCaptureOrder2();
    }
}
