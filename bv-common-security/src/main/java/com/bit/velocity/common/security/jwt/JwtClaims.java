package com.bit.velocity.common.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * JWT token claims structure following BitVelocity security patterns.
 * Contains user identity and authorization information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtClaims {
    
    /**
     * Unique user identifier
     */
    private String userId;
    
    /**
     * Username
     */
    private String username;
    
    /**
     * User email
     */
    private String email;
    
    /**
     * User roles
     */
    private Set<String> roles;
    
    /**
     * User permissions
     */
    private Set<String> permissions;
    
    /**
     * Tenant identifier for multi-tenancy
     */
    private String tenantId;
    
    /**
     * Token type (access, refresh)
     */
    private String tokenType;
    
    /**
     * Token issued at timestamp
     */
    private LocalDateTime issuedAt;
    
    /**
     * Token expiration timestamp
     */
    private LocalDateTime expiresAt;
    
    /**
     * Session identifier for token invalidation
     */
    private String sessionId;
}