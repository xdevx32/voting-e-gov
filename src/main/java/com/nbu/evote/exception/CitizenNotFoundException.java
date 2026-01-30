package com.nbu.evote.exception;

public class CitizenNotFoundException extends RuntimeException {

    public CitizenNotFoundException(String message) {
        super(message);
    }

    public CitizenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
