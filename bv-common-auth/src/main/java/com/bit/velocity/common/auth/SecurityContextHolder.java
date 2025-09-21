package com.bit.velocity.common.auth;

/**
 * Holds the UserContext for the current thread/request.
 * Typically set by authentication filters in downstream services.
 */
class SecurityContextHolder {
  private static final ThreadLocal<UserContext> contextHolder = new ThreadLocal<>();

  /**
   * Sets the current UserContext for this thread/request.
   */
  public static void setContext(UserContext context) {
    contextHolder.set(context);
  }

  /**
   * Retrieves the current UserContext, or null if not set.
   */
  public static UserContext getContext() {
    return contextHolder.get();
  }

  /**
   * Clears the UserContext for this thread/request.
   */
  public static void clearContext() {
    contextHolder.remove();
  }
}

