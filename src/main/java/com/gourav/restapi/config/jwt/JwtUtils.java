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

/**
 * JWT utility for token generation, validation, and invalidation.
 * 
 * Note: The token invalidation (blacklist) uses an in-memory map with TTL.
 * For production with multiple instances, consider implementing with Redis or a database.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${springboot.app.jwtSecret}")
    private String jwtSecret;

    @Value("${springboot.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    /**
     * Stores invalidated tokens with their expiration timestamp (ms).
     * Tokens are removed when they expire to prevent memory leaks.
     */
    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();

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
        // Check if token is blacklisted
        if (isTokenBlacklisted(authToken)) {
            logger.warn("Attempted use of blacklisted token");
            return false;
        }

        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Invalidates a token by adding it to the blacklist.
     * The token is automatically removed from memory after it expires.
     * 
     * @param token JWT token to invalidate
     */
    public void invalidateToken(String token) {
        try {
            // Get expiration time from token
            long expirationMs = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .getTime();

            // Add to blacklist with expiration time
            tokenBlacklist.put(token, expirationMs);
            logger.info("Token invalidated. Will be removed from blacklist at expiration time.");
        } catch (JwtException e) {
            logger.error("Failed to invalidate token: invalid token provided", e);
        }
    }

    /**
     * Checks if a token is blacklisted and cleans up expired entries.
     */
    private boolean isTokenBlacklisted(String token) {
        cleanupExpiredTokens();
        return tokenBlacklist.containsKey(token);
    }

    /**
     * Removes tokens from the blacklist if they have already expired.
     * This prevents unbounded memory growth.
     */
    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        tokenBlacklist.entrySet().removeIf(entry -> entry.getValue() < now);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
