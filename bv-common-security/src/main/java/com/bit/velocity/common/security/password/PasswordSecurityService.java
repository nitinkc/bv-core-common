package com.bit.velocity.common.security.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Password security service providing hashing, validation, and complexity checking.
 * Implements security best practices from ADR-005-security-layering.md
 * 
 * Features:
 * - BCrypt password hashing with salt
 * - Password complexity validation
 * - Secure password comparison
 * - Configurable strength requirements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordSecurityService {
    
    private final PasswordEncoder passwordEncoder;
    
    // Password complexity patterns
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    
    // Default configuration
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;
    
    /**
     * Hash password using BCrypt with salt
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            String hashedPassword = passwordEncoder.encode(plainPassword);
            log.debug("Password hashed successfully");
            return hashedPassword;
        } catch (Exception e) {
            log.error("Error hashing password: {}", e.getMessage());
            throw new RuntimeException("Password hashing failed", e);
        }
    }
    
    /**
     * Verify password against hash
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            log.warn("Null password provided for verification");
            return false;
        }
        
        try {
            boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
            log.debug("Password verification result: {}", matches);
            return matches;
        } catch (Exception e) {
            log.error("Error verifying password: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Validate password complexity
     */
    public PasswordValidationResult validatePasswordComplexity(String password) {
        PasswordValidationResult result = new PasswordValidationResult();
        
        if (password == null) {
            result.addError("Password cannot be null");
            return result;
        }
        
        // Length validation
        if (password.length() < MIN_PASSWORD_LENGTH) {
            result.addError("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        
        if (password.length() > MAX_PASSWORD_LENGTH) {
            result.addError("Password must not exceed " + MAX_PASSWORD_LENGTH + " characters");
        }
        
        // Character type requirements
        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            result.addError("Password must contain at least one uppercase letter");
        }
        
        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            result.addError("Password must contain at least one lowercase letter");
        }
        
        if (!DIGIT_PATTERN.matcher(password).matches()) {
            result.addError("Password must contain at least one digit");
        }
        
        if (!SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            result.addError("Password must contain at least one special character");
        }
        
        // Common password checks
        if (isCommonPassword(password)) {
            result.addError("Password is too common, please choose a different one");
        }
        
        // Sequential character checks
        if (containsSequentialChars(password)) {
            result.addError("Password should not contain sequential characters (e.g., abc, 123)");
        }
        
        return result;
    }
    
    /**
     * Generate a secure random password
     */
    public String generateSecurePassword(int length) {
        if (length < MIN_PASSWORD_LENGTH) {
            length = MIN_PASSWORD_LENGTH;
        }
        
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each category
        password.append(getRandomChar(uppercase));
        password.append(getRandomChar(lowercase));
        password.append(getRandomChar(digits));
        password.append(getRandomChar(specialChars));
        
        // Fill the rest randomly
        String allChars = uppercase + lowercase + digits + specialChars;
        for (int i = 4; i < length; i++) {
            password.append(getRandomChar(allChars));
        }
        
        // Shuffle the password
        return shuffleString(password.toString());
    }
    
    /**
     * Check if password is in common password list
     */
    private boolean isCommonPassword(String password) {
        // Simplified common password check
        String[] commonPasswords = {
                "password", "123456", "password123", "admin", "qwerty",
                "letmein", "welcome", "monkey", "dragon", "password1",
                "123456789", "football", "iloveyou", "admin123", "welcome123"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.equals(common)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check for sequential characters
     */
    private boolean containsSequentialChars(String password) {
        String sequential = "abcdefghijklmnopqrstuvwxyz0123456789";
        String lowerPassword = password.toLowerCase();
        
        for (int i = 0; i <= sequential.length() - 3; i++) {
            String sequence = sequential.substring(i, i + 3);
            if (lowerPassword.contains(sequence)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get random character from string
     */
    private char getRandomChar(String chars) {
        int index = (int) (Math.random() * chars.length());
        return chars.charAt(index);
    }
    
    /**
     * Shuffle string characters
     */
    private String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = (int) (Math.random() * chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }
        return new String(chars);
    }
}