package com.bit.velocity.common.security.jwt;

import com.bit.velocity.common.security.UserContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * JWT token service for BitVelocity authentication.
 * Implements secure token generation, validation, and refresh following security best practices.
 * 
 * Security features:
 * - HMAC-SHA256 signing
 * - Configurable expiration
 * - Claims validation
 * - Token refresh capability
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {
    
    private final JwtProperties jwtProperties;
    
    /**
     * Generate access token for authenticated user
     */
    public String generateAccessToken(UserContext userContext) {
        return generateToken(userContext, "access", jwtProperties.getAccessTokenExpiry().toMinutes());
    }
    
    /**
     * Generate refresh token for token renewal
     */
    public String generateRefreshToken(UserContext userContext) {
        return generateToken(userContext, "refresh", jwtProperties.getRefreshTokenExpiry().toMinutes());
    }
    
    /**
     * Validate and parse JWT token
     */
    public JwtClaims validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setAllowedClockSkewSeconds(jwtProperties.getClockSkew().toSeconds())
                    .requireIssuer(jwtProperties.getIssuer())
                    .requireAudience(jwtProperties.getAudience())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return mapClaimsToJwtClaims(claims);
            
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            throw new JwtException("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw new JwtException("Unsupported token", e);
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new JwtException("Malformed token", e);
        } catch (SecurityException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            throw new JwtException("Token validation failed", e);
        }
    }
    
    /**
     * Extract user context from validated token
     */
    public UserContext extractUserContext(String token) {
        JwtClaims claims = validateToken(token);
        
        UserContext userContext = new UserContext();
        userContext.setUserId(claims.getUserId());
        userContext.setUsername(claims.getUsername());
        userContext.setEmail(claims.getEmail());
        userContext.setRoles(claims.getRoles());
        userContext.setPermissions(claims.getPermissions());
        userContext.setTenantId(claims.getTenantId());
        
        return userContext;
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            JwtClaims claims = validateToken(token);
            return LocalDateTime.now().isAfter(claims.getExpiresAt());
        } catch (JwtException e) {
            return true;
        }
    }
    
    /**
     * Generate JWT token with specified type and expiration
     */
    private String generateToken(UserContext userContext, String tokenType, long expirationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(expirationMinutes);
        
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setSubject(userContext.getUserId())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .claim("username", userContext.getUsername())
                .claim("email", userContext.getEmail())
                .claim("roles", userContext.getRoles())
                .claim("permissions", userContext.getPermissions())
                .claim("tenantId", userContext.getTenantId())
                .claim("tokenType", tokenType)
                .claim("sessionId", UUID.randomUUID().toString())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Map JWT claims to internal claims structure
     */
    @SuppressWarnings("unchecked")
    private JwtClaims mapClaimsToJwtClaims(Claims claims) {
        LocalDateTime issuedAt = claims.getIssuedAt() != null ? 
                LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()) : null;
        LocalDateTime expiresAt = claims.getExpiration() != null ?
                LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()) : null;
        
        return JwtClaims.builder()
                .userId(claims.getSubject())
                .username((String) claims.get("username"))
                .email((String) claims.get("email"))
                .roles((Set<String>) claims.get("roles"))
                .permissions((Set<String>) claims.get("permissions"))
                .tenantId((String) claims.get("tenantId"))
                .tokenType((String) claims.get("tokenType"))
                .sessionId((String) claims.get("sessionId"))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
    }
    
    /**
     * Get signing key for JWT tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
}