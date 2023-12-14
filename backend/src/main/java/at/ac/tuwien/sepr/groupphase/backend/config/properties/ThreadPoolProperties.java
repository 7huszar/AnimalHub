package at.ac.tuwien.sepr.groupphase.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for thread pool used for most likely e-mails.
 */
@Component
@ConfigurationProperties(prefix = "threadpool")
@Getter
@Setter
public class ThreadPoolProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
}
