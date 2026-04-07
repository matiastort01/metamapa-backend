package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.GlobalBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.InternalServerErrorException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

  // ==========================================
  // VALIDACIONES DE ESTRUCTURA (@Valid)
  // ==========================================

  // Para endpoints REACTIVOS (WebFlux)
  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ApiError> handleWebFluxValidation(WebExchangeBindException ex) {
    Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage,
            (msg1, msg2) -> msg1 // Si hay mas de un error para un mismo campo se queda con el primero -> si quisiese concatenar los errores hay que poner esto: (msg1, msg2) -> msg1 + ". " + msg2
        ));

    log.warn("⚠️ Validación fallida (WebFlux). Campos: {}", fields);

    ApiError error = ApiError.of("VALIDATION_ERROR", "Datos de entrada inválidos.", fields);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // Para endpoints CLÁSICOS (MVC Servlet)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMvcValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage,
            (msg1, msg2) -> msg1 // Si hay mas de un error para un mismo campo se queda con el primero -> si quisiese concatenar los errores hay que poner esto: (msg1, msg2) -> msg1 + ". " + msg2
        ));

    log.warn("⚠️ Validación fallida (MVC). Campos: {}", fields);

    ApiError error = ApiError.of("VALIDATION_ERROR", "Datos de entrada inválidos.", fields);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // Para validaciones de @RequestParam y @PathVariable (Ej: @Max, @Min en params)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintValidation(ConstraintViolationException ex) {
    Map<String, String> fields = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> {
              // El path suele venir como "metodo.parametro", queremos solo el parametro
              String path = violation.getPropertyPath().toString();
              return path.substring(path.lastIndexOf('.') + 1);
            },
            ConstraintViolation::getMessage,
            (msg1, msg2) -> msg1
        ));

    log.warn("⚠️ Validación de restricciones (Constraint). Campos: {}", fields);

    ApiError error = ApiError.of("VALIDATION_ERROR", "Parámetros de consulta inválidos.", fields);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // Tipo de dato incorrecto en URL
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String nombreCampo = ex.getName();
    String tipoRequerido = ex.getRequiredType().getSimpleName();
    String valorEnviado = ex.getValue().toString();

    String detalle = String.format("El parámetro '%s' debe ser de tipo %s. Valor recibido: '%s'", nombreCampo, tipoRequerido, valorEnviado);

    log.warn("⚠️ Tipo de dato incorrecto. Campo: '{}' | Esperado: {} | Recibido: '{}'", nombreCampo, tipoRequerido, valorEnviado);

    ApiError error = ApiError.of("BAD_REQUEST", "Tipo de dato incorrecto en parámetros.", List.of(detalle));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // ==========================================
  // ERRORES DE NEGOCIO
  // ==========================================

  // Manejo de errores de Negocio por Campo (422)
  @ExceptionHandler(ValidationBusinessException.class)
  public ResponseEntity<ApiError> handleBusinessValidation(ValidationBusinessException ex) {
    log.warn("✋ Validación de Negocio (Campos): {}", ex.getFieldErrors());

    ApiError error = ApiError.of("BUSINESS_VALIDATION_ERROR",
        "La request contiene errores de validación de negocio.", ex.getFieldErrors());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

  // Manejo de errores Globales (403, 400, etc.)
  @ExceptionHandler(GlobalBusinessException.class)
  public ResponseEntity<ApiError> handleGlobalBusiness(GlobalBusinessException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    if ("FORBIDDEN".equalsIgnoreCase(ex.getCode())) {
      status = HttpStatus.FORBIDDEN;
    }
    else if ("CONFLICT".equalsIgnoreCase(ex.getCode())) {
      status = HttpStatus.CONFLICT;
    }

    log.warn("✋ Regla de Negocio ({}): {} | Status: {}", ex.getCode(), ex.getMessage(), status);

    ApiError error = ApiError.of(ex.getCode(), ex.getMessage(), ex.getDetails());
    return ResponseEntity.status(status).body(error);
  }

  // Manejo de 404
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
    log.warn("🔍 Recurso no encontrado: {}", ex.getMessage());

    ApiError error = ApiError.of("RESOURCE_NOT_FOUND", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // 404
  }

  // ==========================================
  // SEGURIDAD (Spring Security)
  // ==========================================

  // 403 Forbidden - Usuario autenticado pero sin permisos
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
    log.error("⛔ Acceso Denegado (403): {}", ex.getMessage());

    ApiError error = ApiError.of("FORBIDDEN", "No tienes permisos para realizar esta acción.", List.of(ex.getMessage()));
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  // 401 Unauthorized - Token inválido o faltante
  // Nota: A veces Spring Security maneja esto en el filtro antes de llegar aquí (ServerAuthenticationEntryPoint).
  // Pero si llega al controller, lo atrapamos.
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthenticationError(AuthenticationException ex) {
    log.warn("🔒 Fallo de Autenticación (401): {}", ex.getMessage());

    ApiError error = ApiError.of("UNAUTHORIZED", "Falló la autenticación.", List.of(ex.getMessage()));
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  // ==========================================
  // ERRORES DE INFRAESTRUCTURA
  // ==========================================

  // JSON Roto, tipos incorrectos (WebFlux) (VIENE CON SPRING)
  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity<ApiError> handleBadInput(ServerWebInputException ex) {
    log.warn("⚠️ Request malformada (JSON inválido o faltante): {}", ex.getReason());

    String razon = ex.getReason();
    ApiError error = ApiError.of("BAD_REQUEST", "Formato inválido", List.of(razon));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // ==========================================
  // ERRORES INTERNOS EXPLÍCITOS (500)
  // ==========================================

  // Errores que nosotros lanzamos intencionalmente porque algo falló en el servidor
  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ApiError> handleInternalServerCustom(InternalServerErrorException ex) {
    // Logueamos con la causa (si existe)
    log.error("💥 Error Interno Explícito: {}", ex.getMessage(), ex);

    // Acá SÍ usamos el mensaje de la excepción porque es un mensaje controlado por nosotros
    ApiError error = ApiError.of("INTERNAL_ERROR", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  // ==========================================
  // OTROS
  // ==========================================

  // Catch-All (Cualquier cosa no manejada)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {
    // Extraigo el contexto para el log (Request info)
    String method = request.getMethod(); // Ej: GET, POST
    String path = request.getRequestURI(); // Ej: /api/hechos
    String params = request.getQueryString(); // Ej: page=0&size=10 (puede ser null)

    // Opcional: Obtener usuario (si usas Spring Security Reactive)
    // String user = exchange.getPrincipal().map(Principal::getName).defaultIfEmpty("ANONYMOUS").block();
    // Nota: block() aquí es seguro si el error ya ocurrió, pero idealmente se maneja reactivo o se ignora en logs simples.

    // Logueo todo (El error 'ex' va AL FINAL para que imprima el StackTrace)
    log.error("🔥 ERROR INTERNO NO CONTROLADO en [{} {}] | Params: {} | Mensaje: {}",
        method,
        path,
        (params != null ? params : "N/A"),
        ex.getMessage(),
        ex);

    // Respuesta al cliente (Sin detalles técnicos sensibles)
    ApiError error = ApiError.of("INTERNAL_SERVER_ERROR", "Ocurrió un error inesperado en el servidor.");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
