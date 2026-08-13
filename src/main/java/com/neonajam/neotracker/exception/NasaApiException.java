package com.neonajam.neotracker.exception;

public class NasaApiException extends RuntimeException {

    public NasaApiException (String message, Throwable cause) {
        super(message, cause);
    }
}
