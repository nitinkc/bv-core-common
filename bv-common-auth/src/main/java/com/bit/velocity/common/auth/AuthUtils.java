package com.bit.velocity.common.auth;

/**
 * Utility class for common authentication-related helpers.
 * Extend this class with static methods as needed (e.g., JWT parsing, role checks).
 */
public class AuthUtils {
    /**
     * Checks if the current user has a specific role.
     *
     * @param role the role to check (e.g., "ADMIN")
     * @return true if the user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        UserContext ctx = SecurityContextHolder.getContext();
        if (ctx == null || ctx.getRoles() == null) return false;
        for (String r : ctx.getRoles()) {
            if (role.equals(r)) return true;
        }
        return false;
    }

    /**
     * Returns the current authenticated user's ID, or null if not authenticated.
     */
    public static String getCurrentUserId() {
        UserContext ctx = SecurityContextHolder.getContext();
        return ctx != null ? ctx.getUserId() : null;
    }

    /**
     * Returns true if a user is authenticated (i.e., UserContext is present).
     */
    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext() != null;
    }

    /**
     * Returns the username of the current user, or null if not authenticated.
     */
    public static String getCurrentUsername() {
        UserContext ctx = SecurityContextHolder.getContext();
        return ctx != null ? ctx.getUsername() : null;
    }
}

