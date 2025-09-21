package com.bit.velocity.common.security;

import lombok.Data;

import java.util.Set;

/**
 * User context for security and audit tracking.
 * Follows security layering defined in ADR-005-security-layering.md
 */
@Data
public class UserContext {
    
    private String userId;
    private String username;
    private String email;
    private Set<String> roles;
    private Set<String> permissions;
    private String tenantId;
    
    /**
     * Check if user has a specific role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Check if user has a specific permission
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * Check if user has any of the specified roles
     */
    public boolean hasAnyRole(String... roles) {
        if (this.roles == null || roles == null) {
            return false;
        }
        for (String role : roles) {
            if (this.roles.contains(role)) {
                return true;
            }
        }
        return false;
    }
}