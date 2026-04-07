package metaMapa.usuario;

public class InfoContribuyente {
  private String nombre;
  private String apellido;
  private Integer edad;

  public InfoContribuyente(String nombre, String apellido, Integer edad) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre es obligatorio si se proporcionan datos personales.");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
  }
}