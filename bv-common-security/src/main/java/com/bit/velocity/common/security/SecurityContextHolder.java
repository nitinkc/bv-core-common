package com.bit.velocity.common.security;

/**
 * Thread-local storage for user context throughout request processing.
 * Enables audit tracking and security checks across all layers.
 */
public class SecurityContextHolder {
    
    private static final ThreadLocal<UserContext> userContextHolder = new ThreadLocal<>();
    
    /**
     * Set the current user context for the thread
     */
    public static void setUserContext(UserContext userContext) {
        userContextHolder.set(userContext);
    }
    
    /**
     * Get the current user context
     */
    public static UserContext getUserContext() {
        return userContextHolder.get();
    }
    
    /**
     * Clear the user context (important for thread pool cleanup)
     */
    public static void clear() {
        userContextHolder.remove();
    }
    
    /**
     * Get current user ID for audit purposes
     */
    public static String getCurrentUserId() {
        UserContext context = getUserContext();
        return context != null ? context.getUserId() : "system";
    }
    
    /**
     * Get current username for audit purposes
     */
    public static String getCurrentUsername() {
        UserContext context = getUserContext();
        return context != null ? context.getUsername() : "system";
    }
}