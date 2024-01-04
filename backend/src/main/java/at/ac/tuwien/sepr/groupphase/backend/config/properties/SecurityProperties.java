package at.ac.tuwien.sepr.groupphase.backend.config.properties;

import at.ac.tuwien.sepr.groupphase.backend.config.SecurityPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * This configuration class offers all necessary security properties in an immutable manner.
 */
@Component
@RequiredArgsConstructor
public class SecurityProperties {

    private final SecurityPropertiesConfig.Auth auth;
    private final SecurityPropertiesConfig.Jwt jwt;
    private final SecurityPropertiesConfig.ActJwt actJwt;
    private final SecurityPropertiesConfig.PwdJwt pwdJwt;

    public String getAuthHeader() {
        return auth.getHeader();
    }

    public String getAuthTokenPrefix() {
        return auth.getPrefix();
    }

    public String getLoginUri() {
        return auth.getLoginUri();
    }

    public String getJwtSecret() {
        return jwt.getSecret();
    }

    public String getJwtType() {
        return jwt.getType();
    }

    public String getJwtIssuer() {
        return jwt.getIssuer();
    }

    public String getJwtAudience() {
        return jwt.getAudience();
    }

    public Long getJwtExpirationTime() {
        return jwt.getExpirationTime();
    }

    public String getActJwtSecret() {
        return actJwt.getSecret();
    }

    public String getActJwtType() {
        return actJwt.getType();
    }

    public String getActJwtIssuer() {
        return actJwt.getIssuer();
    }

    public String getActJwtAudience() {
        return actJwt.getAudience();
    }

    public Long getActJwtExpirationTime() {
        return actJwt.getExpirationTime();
    }

    public String getPwdJwtSecret() {
        return pwdJwt.getSecret();
    }

    public String getPwdJwtType() {
        return pwdJwt.getType();
    }

    public String getPwdJwtIssuer() {
        return pwdJwt.getIssuer();
    }

    public String getPwdJwtAudience() {
        return pwdJwt.getAudience();
    }

    public Long getPwdJwtExpirationTime() {
        return pwdJwt.getExpirationTime();
    }

}
