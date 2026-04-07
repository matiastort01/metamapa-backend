package __tpa_mi_no_grupo_22.gestion_usuarios.exceptions;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String entidad, String id) {
    super("No se ha encontrado " + entidad + " de id " + id);
  }
}
