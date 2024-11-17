package com.artisan.backend.exceptions;

public class UnhandledRejection extends RuntimeException {
    public UnhandledRejection(String message) {
        super(message);
    }
}
