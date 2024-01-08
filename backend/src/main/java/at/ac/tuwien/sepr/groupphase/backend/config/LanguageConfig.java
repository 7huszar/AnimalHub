package at.ac.tuwien.sepr.groupphase.backend.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/i18n/lang.properties")
@AllArgsConstructor
@Component
public class LanguageConfig {

    private final Environment env;

    public String getTranslation(String key) {
        return env.getProperty(key);
    }

}
