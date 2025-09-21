package com.bit.velocity.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to require specific roles for method access.
 * Works with Spring Security and BitVelocity security context.
 * 
 * Example usage:
 * @RequireRole("ADMIN")
 * @RequireRole({"ADMIN", "PRODUCT_MANAGER"})
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    
    /**
     * Required roles (any of these roles will allow access)
     */
    String[] value();
    
    /**
     * Whether to require ALL roles (true) or ANY role (false, default)
     */
    boolean requireAll() default false;
}