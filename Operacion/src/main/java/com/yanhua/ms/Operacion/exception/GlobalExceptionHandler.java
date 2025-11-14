package com.yanhua.ms.Operacion.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores cuando el JSON recibido no puede ser parseado.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        logger.error("[JSON_PARSING_ERROR] → JSON mal formado en request: {} | Causa: {}", 
                request.getDescription(false), ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
        logger.debug("[STACK_TRACE]", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud mal formada (JSON inválido)",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 400 Bad Request por JSON inválido");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de campos (Bean Validation).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
            WebRequest request) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Datos inválidos");
        
        logger.warn("[VALIDATION_ERROR] → Error de validación en request: {} | Detalles: {}", 
                request.getDescription(false), details);
        logger.debug("[VALIDATION_FIELDS] → {}", ex.getBindingResult().getFieldErrors());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación de campos",
                details,
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 400 Bad Request por validación fallida");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores por parámetros de ruta o query con tipo incorrecto.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @SuppressWarnings("null")
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        Class<?> requiredTypeClass = ex.getRequiredType();
        String requiredType = (requiredTypeClass != null) ? requiredTypeClass.getSimpleName() : "Unknown";
        
        logger.warn("[TYPE_MISMATCH_ERROR] → Tipo de parámetro incorrecto en request: {} | Parámetro: '{}' | Tipo esperado: {}", 
                request.getDescription(false), ex.getName(), requiredType);
        logger.debug("[TYPE_MISMATCH_VALUE] → Valor recibido: {}", ex.getValue());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro con tipo incorrecto",
                "El parámetro '" + ex.getName() + "' debe ser del tipo " + requiredType,
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 400 Bad Request por tipo de parámetro incorrecto");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de recursos no encontrados.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("[RESOURCE_NOT_FOUND] → Recurso no encontrado en request: {} | Mensaje: {}", 
                request.getDescription(false), ex.getMessage());
        logger.debug("[NOT_FOUND_DETAILS] → {}", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 404 Not Found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja violaciones de integridad de datos (clave única, null, foreign key,
     * etc.).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        String detailedMessage = ex.getMostSpecificCause() != null ? 
                ex.getMostSpecificCause().getMessage() : ex.getMessage();
        
        logger.error("[DATA_INTEGRITY_VIOLATION] → Violación de integridad de datos en request: {} | Causa: {}", 
                request.getDescription(false), detailedMessage);
        logger.error("[DATA_INTEGRITY_ROOT_CAUSE] → {}", ex);
        logger.debug("[STACK_TRACE]", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Violación de integridad de datos",
                detailedMessage,
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 409 Conflict por violación de integridad");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja errores de acceso a la base de datos.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException ex, WebRequest request) {
        String detailedMessage = ex.getMostSpecificCause() != null ? 
                ex.getMostSpecificCause().getMessage() : ex.getMessage();
        
        logger.error("[DATABASE_ACCESS_ERROR] → Error de acceso a base de datos en request: {} | Causa: {}", 
                request.getDescription(false), detailedMessage);
        logger.error("[DATABASE_ERROR_ROOT_CAUSE] → {}", ex);
        logger.debug("[STACK_TRACE]", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error al acceder a la base de datos",
                detailedMessage,
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 500 Internal Server Error por error de base de datos");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja transacciones duplicadas.
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateKeyException ex, WebRequest request) {
        logger.warn("[DUPLICATE_KEY_ERROR] → Transacción duplicada detectada en request: {} | Causa: {}", 
                request.getDescription(false), ex.getMessage());
        logger.debug("[DUPLICATE_DETAILS] → {}", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Transacción duplicada",
                ex.getMessage(),
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 409 Conflict por transacción duplicada");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja cualquier otra excepción no controlada.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("[UNCAUGHT_EXCEPTION] → Error no controlado en request: {} | Tipo: {} | Mensaje: {}", 
                request.getDescription(false), ex.getClass().getName(), ex.getMessage());
        logger.error("[UNCAUGHT_EXCEPTION_ROOT_CAUSE] → {}", ex);
        logger.debug("[STACK_TRACE]", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                ex.getMessage(),
                request.getDescription(false));
        
        logger.info("[RESPONSE] → Retornando error 500 Internal Server Error por excepción no controlada");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
