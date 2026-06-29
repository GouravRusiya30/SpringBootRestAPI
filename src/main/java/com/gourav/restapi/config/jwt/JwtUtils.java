package com.gourav.restapi.config.jwt;

import com.gourav.restapi.config.services.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${springboot.app.jwtSecret}")
    private String jwtSecret;

    @Value("${springboot.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final Map<String, Boolean> jwtMap = new ConcurrentHashMap<>();

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();

        jwtMap.put(token, false);
        return token;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        if (jwtMap.containsKey(authToken)) {
            try {
                Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
                return true;
            } catch (JwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty: {}", e.getMessage());
            }
        }
        return false;
    }

    public void invalidateToken(String token) {
        jwtMap.remove(token);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
