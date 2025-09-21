package com.bit.velocity.common.security.password;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of password validation containing validation status and error messages.
 */
@Data
public class PasswordValidationResult {
    
    private List<String> errors = new ArrayList<>();
    private boolean valid = true;
    
    /**
     * Add validation error
     */
    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }
    
    /**
     * Check if password is valid
     */
    public boolean isValid() {
        return valid && errors.isEmpty();
    }
    
    /**
     * Get validation errors as formatted string
     */
    public String getErrorsAsString() {
        return String.join("; ", errors);
    }
    
    /**
     * Get number of validation errors
     */
    public int getErrorCount() {
        return errors.size();
    }
}