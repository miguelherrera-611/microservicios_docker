package com.gaming.gameservice.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message) {
        super(message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameNotFoundException(Long id) {
        super("Juego no encontrado con ID: " + id);
    }

    public GameNotFoundException(String field, String value) {
        super("Juego no encontrado con " + field + ": " + value);
    }
}