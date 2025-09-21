package com.bit.velocity.common.exception;

import lombok.Getter;

/**
 * Base exception for all BitVelocity domain exceptions.
 * Provides structured error handling across all services.
 */
@Getter
public class BitVelocityException extends RuntimeException {
    
    private final String errorCode;
    private final String domain;
    
    public BitVelocityException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.domain = "common";
    }
    
    public BitVelocityException(String errorCode, String domain, String message) {
        super(message);
        this.errorCode = errorCode;
        this.domain = domain;
    }
    
    public BitVelocityException(String errorCode, String domain, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.domain = domain;
    }
}