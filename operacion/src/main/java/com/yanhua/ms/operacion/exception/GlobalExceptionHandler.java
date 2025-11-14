package com.yanhua.ms.operacion.exception;

import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Maneja JSON inválido en el body (malformed JSON) -> 400 Bad Request.
         *
         * Cuándo se ejecuta:
         * - Cuando Spring no puede leer/convertir el body de la petición al objeto Java esperado
         *   (por ejemplo JSON mal formado, tipos incompatibles o errores de parseo).
         * - Ocurre en la fase de deserialización/binding, antes de invocar el controlador.
         *
         * Qué registra y devuelve:
         * - Registra un `ERROR` con el mensaje de la excepción y, si procede, la causa más específica.
         * - Devuelve un `ErrorResponse` (construido vía constructor) que incluye: `timestamp`, `status` = 400,
         *   `error` con texto en español, `message` con el detalle de la causa y `path` con la descripción
         *   de la petición (`request.getDescription(false)`).
         * - Código HTTP: 400 Bad Request.
         */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {

        String description = request.getDescription(false);
        logger.error("[Request] → JSON mal formado: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud mal formada (JSON inválido)",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                description);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

        /**
         * Maneja errores de validación de argumentos (@Valid) -> 400 Bad Request.
         *
         * Cuándo se ejecuta:
         * - Tras el binding de la petición cuando las anotaciones de validación en el DTO
         *   (por ejemplo `@NotNull`, `@Size`, `@Positive`) no se cumplen.
         * - Normalmente ocurre en controladores con `@Valid @RequestBody`.
         *
         * Qué registra y devuelve:
         * - Registra un `WARN` con la lista de campos inválidos y sus mensajes.
         * - Devuelve un `ErrorResponse` (vía builder) con `timestamp`, `status` = 400,
         *   `error` = "Solicitud inválida" y `message` con la concatenación de errores
         *   en formato `campo: mensaje; campo2: mensaje2`.
         * - Código HTTP: 400 Bad Request.
         *
         * Ejemplo de `message` devuelto: "Validación fallida: idCliente: no debe ser nulo; total: debe ser positivo"
         */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
            WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Validación fallida para la petición {}: {}", path, errors);

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Solicitud inválida")
                .message("Validación fallida: " + errors)
                .path(path)
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

        /**
         * Maneja violaciones de restricciones de la capa de validación
         * (ConstraintViolationException) -> 400 Bad Request.
         *
         * Cuándo se ejecuta:
         * - Al fallar validaciones declaradas con Bean Validation (p.ej. en parámetros
         *   de métodos validados con `@Validated`) o en validaciones programáticas que
         *   lanzan `ConstraintViolationException`.
         *
         * Qué registra y devuelve:
         * - Registra un `WARN` con cada violación (propiedad: mensaje).
         * - Devuelve un `ErrorResponse` con `status = 400`, `error = "Solicitud inválida"`
         *   y `message` con la lista de violaciones.
         */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
            HttpServletRequest request) {

        String path = request.getRequestURI();
        String msg = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Violaciones de restricciones en {}: {}", path, msg);
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Solicitud inválida")
                .message("Violaciones de restricciones: " + msg)
                .path(path)
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

        /**
         * Maneja violaciones de integridad de la base de datos (p.ej. unique constraint) -> 409 Conflict.
         *
         * Cuándo se ejecuta:
         * - Cuando la base de datos rechaza una operación por violar restricciones (únicas,
         *   claves foráneas, not-null, etc.) y Spring lanza `DataIntegrityViolationException`.
         *
         * Qué registra y devuelve:
         * - Registra un `ERROR` incluyendo la excepción para diagnóstico (stacktrace).
         * - Devuelve un `ErrorResponse` con `status = 409` (Conflicto) y un mensaje que
         *   incluye la causa más específica proporcionada por el driver/BD.
         */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String path = request.getRequestURI();
        logger.error("Violación de integridad de datos en {}: {}", path, ex.getMessage(), ex);
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflicto")
                .message("Violación de integridad en la base de datos: " + ex.getMostSpecificCause().getMessage())
                .path(path)
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

        /**
         * Maneja errores de acceso a datos en general (DataAccessException) -> 500 Internal Server Error.
         *
         * Cuándo se ejecuta:
         * - Para errores de la capa de persistencia como fallos de conexión, timeouts JDBC,
         *   problemas de recursos o excepciones que se traducen a `DataAccessException`.
         *
         * Qué registra y devuelve:
         * - Registra un `ERROR` con el stacktrace para facilitar la depuración.
         * - Devuelve un `ErrorResponse` genérico con `status = 500` para no exponer detalles internos.
         */
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        logger.error("Error de acceso a datos en {}: {}", path, ex.getMessage(), ex);
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error de datos")
                .message("Error de acceso a datos")
                .path(path)
                .build();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

        /**
         * Maneja `IllegalArgumentException` lanzada por la aplicación -> 400 Bad Request.
         *
         * Cuándo se ejecuta:
         * - Cuando la lógica interna detecta argumentos inválidos y lanza `IllegalArgumentException`.
         *
         * Qué registra y devuelve:
         * - Registra un `WARN` con el mensaje de la excepción.
         * - Devuelve un `ErrorResponse` con `status = 400` y el mensaje de la excepción.
         */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        String path = request.getRequestURI();
        logger.warn("Argumento inválido en {}: {}", path, ex.getMessage());
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Solicitud inválida")
                .message(ex.getMessage())
                .path(path)
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

        /**
         * Manejador por defecto para excepciones no esperadas -> 500 Internal Server Error.
         *
         * Cuándo se ejecuta:
         * - Captura cualquier excepción no manejada por handlers más específicos.
         *
         * Qué registra y devuelve:
         * - Registra un `ERROR` con el stacktrace completo para diagnóstico.
         * - Devuelve un `ErrorResponse` con `status = 500` y un mensaje genérico para el cliente.
         */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        logger.error("Excepción no controlada en {}: {}", path, ex.getMessage());
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .message("Ocurrió un error inesperado")
                .path(path)
                .build();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
