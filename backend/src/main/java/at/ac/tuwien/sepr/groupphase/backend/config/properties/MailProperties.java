package at.ac.tuwien.sepr.groupphase.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail")
@Component
public class MailProperties {
    private String email;
}
