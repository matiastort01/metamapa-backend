package metaMapa.hechos;

import lombok.Data;

@Data
public class Categoria {
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }
}
