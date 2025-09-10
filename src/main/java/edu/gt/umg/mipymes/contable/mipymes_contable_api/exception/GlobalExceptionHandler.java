// ========== GlobalExceptionHandler.java ==========
package edu.gt.umg.mipymes.contable.mipymes_contable_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API
 * Convierte las excepciones en respuestas HTTP apropiadas
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Manejar errores de cuenta no encontrada
     */
    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCuentaNotFound(CuentaNotFoundException ex) {
        log.error("Cuenta no encontrada: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Cuenta no encontrada",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Manejar errores de código duplicado
     */
    @ExceptionHandler(CodigoYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCodigoYaExiste(CodigoYaExisteException ex) {
        log.error("Código duplicado: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Código duplicado",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Manejar errores de validación (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Errores de validación: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Errores de validación",
            errors,
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Manejar cualquier otra excepción no prevista
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: ", ex);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno del servidor",
            "Ha ocurrido un error inesperado",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========== CLASES PARA RESPUESTAS DE ERROR ==========

    /**
     * Estructura para respuestas de error simples
     */
    public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
    ) {}

    /**
     * Estructura para respuestas de errores de validación
     */
    public record ValidationErrorResponse(
        int status,
        String error,
        Map<String, String> validationErrors,
        LocalDateTime timestamp
    ) {}
}