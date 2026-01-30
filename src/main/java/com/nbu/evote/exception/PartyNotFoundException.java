package com.nbu.evote.exception;

public class PartyNotFoundException extends RuntimeException {

    public PartyNotFoundException(String message) {
        super(message);
    }

    public PartyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
