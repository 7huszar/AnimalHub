package at.ac.tuwien.sepr.groupphase.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "timeouts")
public class TimeoutProperties {
    private int email;
    private int pwd;
    private int login;
}
