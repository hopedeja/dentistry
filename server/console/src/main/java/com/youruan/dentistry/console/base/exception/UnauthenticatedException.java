package com.youruan.dentistry.console.base.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(String message) {
        super(message);
    }

    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
