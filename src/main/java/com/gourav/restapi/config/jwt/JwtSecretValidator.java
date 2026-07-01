package com.gourav.restapi.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Validates JWT configuration on application startup.
 * Prevents production deployments with weak or default secrets.
 */
@Component
public class JwtSecretValidator {

    private static final String DEFAULT_SECRET = "change-this-development-secret-change-this-development-secret";
    private static final int MIN_SECRET_LENGTH = 32; // Required for HS512

    @Value("${springboot.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    @PostConstruct
    public void validate() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalArgumentException(
                    "FATAL: JWT_SECRET environment variable is not set. "
                    + "Set JWT_SECRET before running the application.");
        }

        if (jwtSecret.equals(DEFAULT_SECRET)) {
            boolean isProd = activeProfiles != null && activeProfiles.contains("prod");
            if (isProd) {
                throw new IllegalArgumentException(
                        "FATAL: JWT_SECRET is set to the default value in production mode. "
                        + "Set a strong, unique JWT_SECRET environment variable before deploying to production.");
            } else {
                // Log warning in non-prod environments
                System.err.println("⚠️  WARNING: Using default JWT secret. This is only acceptable for development.");
            }
        }

        if (jwtSecret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "FATAL: JWT_SECRET must be at least %d characters for HS512 algorithm. "
                            + "Current length: %d. Set a longer secret.",
                            MIN_SECRET_LENGTH, jwtSecret.length()));
        }
    }
}
