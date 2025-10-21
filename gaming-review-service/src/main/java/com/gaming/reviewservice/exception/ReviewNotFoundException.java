package com.gaming.reviewservice.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewNotFoundException(Long id) {
        super("Reseña no encontrada con ID: " + id);
    }

    public ReviewNotFoundException(String field, String value) {
        super("Reseña no encontrada con " + field + ": " + value);
    }
}