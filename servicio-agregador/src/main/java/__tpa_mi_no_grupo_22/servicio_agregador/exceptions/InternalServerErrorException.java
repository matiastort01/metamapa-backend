package __tpa_mi_no_grupo_22.servicio_agregador.exceptions;

import java.util.List;

public class InternalServerErrorException extends RuntimeException {

  // Constructor 1: Solo mensaje (ej: "Falló la generación del PDF")
  public InternalServerErrorException(String message) {
    super(message);
  }

  // Constructor 2: Mensaje + Causa (ej: "Falló encriptación" + la excepción original)
  // ESTE ES CLAVE para no perder el StackTrace original en los logs
  public InternalServerErrorException(String message, Throwable cause) {
    super(message, cause);
  }
}
