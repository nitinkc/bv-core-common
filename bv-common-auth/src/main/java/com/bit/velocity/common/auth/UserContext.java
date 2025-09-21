package com.bit.velocity.common.auth;

/**
 * Provides access to the current authenticated user's context.
 * This class can be extended to include more user details as needed.
 */
public class UserContext {
    // The unique identifier of the authenticated user
    private final String userId;
    // The username or email of the authenticated user
    private final String username;
    // The roles assigned to the user (e.g., USER, ADMIN)
    private final String[] roles;

    public UserContext(String userId, String username, String[] roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String[] getRoles() {
        return roles;
    }
}