package com.bit.velocity.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for password encoding and basic security settings.
 * Provides BCrypt password encoder with secure defaults.
 */
@Configuration
public class SecurityConfiguration {
    
    /**
     * BCrypt password encoder with strength 12 for secure password hashing.
     * Higher strength means more secure but slower hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}