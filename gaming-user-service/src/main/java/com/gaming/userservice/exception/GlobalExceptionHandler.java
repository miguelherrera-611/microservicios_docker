package com.gaming.userservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de usuarios no encontrados
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("User not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "USER_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    // Manejo de argumentos ilegales (duplicados, credenciales inválidas, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    // Manejo de errores de validación
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationErrorResponse>> handleValidationException(WebExchangeBindException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Error de validación en los datos del usuario",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    // Manejo de errores de autenticación
    @ExceptionHandler(SecurityException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleSecurityException(SecurityException ex) {
        logger.error("Security error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "AUTHENTICATION_ERROR",
                "Error de autenticación: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
    }

    // Manejo de errores de runtime genéricos
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime error: {}", ex.getMessage(), ex);

        // Casos específicos de runtime exceptions
        if (ex.getMessage().contains("Credenciales inválidas")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "INVALID_CREDENTIALS",
                    "Credenciales inválidas",
                    HttpStatus.UNAUTHORIZED.value(),
                    LocalDateTime.now()
            );
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
        }

        if (ex.getMessage().contains("Contraseña actual incorrecta")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "INVALID_PASSWORD",
                    "La contraseña actual es incorrecta",
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now()
            );
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_ERROR",
                "Error interno del servidor: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    // Manejo de errores genéricos
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                "UNEXPECTED_ERROR",
                "Error inesperado del servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    // Clase para respuestas de error básicas
    public static class ErrorResponse {
        private String code;
        private String message;
        private int status;
        private LocalDateTime timestamp;

        public ErrorResponse(String code, String message, int status, LocalDateTime timestamp) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
        }

        // Getters y Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    // Clase para respuestas de errores de validación
    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> fieldErrors;

        public ValidationErrorResponse(String code, String message, int status,
                                       LocalDateTime timestamp, Map<String, String> fieldErrors) {
            super(code, message, status, timestamp);
            this.fieldErrors = fieldErrors;
        }

        public Map<String, String> getFieldErrors() { return fieldErrors; }
        public void setFieldErrors(Map<String, String> fieldErrors) { this.fieldErrors = fieldErrors; }
    }
}