    package yanhua.usuarios.exception;

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
            logger.error("[Request] → JSON mal formado: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Solicitud mal formada (JSON inválido)",
                    ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        /**
         * Maneja errores de validación de campos (Bean Validation).
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
            logger.error("[Request] → Error de validación: {}", ex.getMessage());

            String details = ex.getBindingResult().getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Datos inválidos");

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Error de validación de campos",
                    details,
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        /**
         * Maneja errores por parámetros de ruta o query con tipo incorrecto.
         */
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
            logger.error("[Request] → Tipo de parámetro incorrecto: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Parámetro con tipo incorrecto",
                    "El parámetro '" + ex.getName() + "' debe ser del tipo " + ex.getRequiredType().getSimpleName(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        /**
         * Maneja excepciones de recursos no encontrados.
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
            logger.warn("[DB] → Recurso no encontrado: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Recurso no encontrado",
                    ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        /**
         * Maneja violaciones de integridad de datos (clave única, null, foreign key, etc.).
         */
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
            logger.error("[DB] → Violación de integridad de datos: {}", ex.getMostSpecificCause().getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(),
                    "Violación de integridad de datos",
                    ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
            logger.warn("[Auth] → Acceso no autorizado: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.UNAUTHORIZED.value(),
                    "Acceso incorrecto",
                    ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        /**
         * Maneja errores de acceso a la base de datos.
         */
        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException ex, WebRequest request) {
            logger.error("[DB] → Error de acceso a base de datos: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al acceder a la base de datos",
                    ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /**
         * Maneja cualquier otra excepción no controlada.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
            logger.error("[Global] → Error no controlado: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error interno del servidor",
                    ex.getMessage(),
                    request.getDescription(false));
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Transacción duplicada
        @ExceptionHandler(DuplicateKeyException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateKeyException  ex, WebRequest request) {
            logger.warn("[GlobalExceptionHandler] → Transacción duplicada: {}", ex.getMessage());

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(),
                    "Transacción duplicada",
                    ex.getMessage(),
                    request.getDescription(false)
            );
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }
