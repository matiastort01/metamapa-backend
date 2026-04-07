package __tpa_mi_no_grupo_22.fuente_dinamica.exceptions;

import java.util.List;

// Excepción para reglas de negocio atómicas/globales (ej. palabra prohibida)
public class GlobalBusinessException extends RuntimeException {
  private final String code;
  private final List<String> details;

  public GlobalBusinessException(String code, String message, List<String> details) {
    super(message);
    this.code = code;
    this.details = details;
  }

  public String getCode() {
    return code;
  }

  public List<String> getDetails() {
    return details;
  }
}