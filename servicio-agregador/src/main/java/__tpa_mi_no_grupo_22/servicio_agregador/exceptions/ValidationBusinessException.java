package __tpa_mi_no_grupo_22.servicio_agregador.exceptions;

import java.util.Map;

// Excepción para la acumulación de errores de campo de negocio
// Llevaremos el mapa de errores del Service al ControllerAdvice.
public class ValidationBusinessException extends RuntimeException {
  private final Map<String, String> fieldErrors;

  public ValidationBusinessException(Map<String, String> fieldErrors) {
    super("La validación de negocio falló.");
    this.fieldErrors = fieldErrors;
  }

  public Map<String, String> getFieldErrors() {
    return fieldErrors;
  }
}
