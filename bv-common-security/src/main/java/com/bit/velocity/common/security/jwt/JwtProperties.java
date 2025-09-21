package com.bit.velocity.common.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * JWT configuration properties.
 * Follows security layering patterns from ADR-005-security-layering.md
 */
@Data
@Component
@ConfigurationProperties(prefix = "bitvelocity.security.jwt")
public class JwtProperties {
    
    /**
     * Secret key for signing JWT tokens
     * In production, this should come from environment variables or secret management
     */
    private String secret = "bitvelocity-default-secret-key-change-in-production-minimum-256-bits-required-for-hs256";
    
    /**
     * Access token expiration time
     */
    private Duration accessTokenExpiry = Duration.ofHours(1);
    
    /**
     * Refresh token expiration time
     */
    private Duration refreshTokenExpiry = Duration.ofDays(7);
    
    /**
     * JWT token issuer
     */
    private String issuer = "bitvelocity";
    
    /**
     * JWT audience
     */
    private String audience = "bitvelocity-api";
    
    /**
     * Whether to validate token expiration
     */
    private boolean validateExpiration = true;
    
    /**
     * Clock skew tolerance for token validation
     */
    private Duration clockSkew = Duration.ofMinutes(1);
}