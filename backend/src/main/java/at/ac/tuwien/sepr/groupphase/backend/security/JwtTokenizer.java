package at.ac.tuwien.sepr.groupphase.backend.security;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenizer {

    private final SecurityProperties securityProperties;

    public String getAuthToken(String user, List<String> roles) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getJwtType())
            .setIssuer(securityProperties.getJwtIssuer())
            .setAudience(securityProperties.getJwtAudience())
            .setSubject(user)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
            .claim("rol", roles)
            .compact();
        return securityProperties.getAuthTokenPrefix() + token;
    }


    public String getActivationToken(String user, String language) {
        byte[] signingKey = securityProperties.getActJwtSecret().getBytes();
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getActJwtType())
            .setIssuer(securityProperties.getActJwtIssuer())
            .setAudience(securityProperties.getActJwtAudience())
            .setSubject("verification")
            .claim("email", user)
            .claim("language", language)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getActJwtExpirationTime()))
            .compact();
    }

    public String getPasswordToken(String user, String language) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getActJwtType())
            .setIssuer(securityProperties.getActJwtIssuer())
            .setAudience(securityProperties.getActJwtAudience())
            .setSubject("verification")
            .claim("email", user)
            .claim("language", language)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getActJwtExpirationTime()))
            .compact();
        return token;

    }

}
