package at.ac.tuwien.sepr.groupphase.backend.mail;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.TimeoutProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BlockableService {

    private final TimeoutProperties timeoutProperties;

    public Blockable blockedIp(String ip) {
        return new BlockedIp(ip, timeoutProperties.getLogin());
    }

    public Blockable blockedEmail(String email) {
        return new StampedEmail(email, timeoutProperties.getEmail());
    }

    public Blockable blockedPwd(String email) {
        return new StampedEmail(email, timeoutProperties.getPwd());
    }

}
