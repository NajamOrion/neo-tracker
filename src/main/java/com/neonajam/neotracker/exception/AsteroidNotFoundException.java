package com.neonajam.neotracker.exception;

public class AsteroidNotFoundException extends RuntimeException {

    public AsteroidNotFoundException(Long id) {
        super("Asteroid not found with id: " + id);
    }
}