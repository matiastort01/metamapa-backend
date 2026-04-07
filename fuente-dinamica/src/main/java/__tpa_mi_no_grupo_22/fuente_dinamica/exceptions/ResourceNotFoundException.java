package __tpa_mi_no_grupo_22.fuente_dinamica.exceptions;

// Excepción para recursos no encontrados (Mapeo a 404)
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}