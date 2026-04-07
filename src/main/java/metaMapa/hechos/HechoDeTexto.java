package metaMapa.hechos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import metaMapa.usuario.InfoContribuyente;
import metaMapa.utils.Ubicacion;

@Data
@Builder
public class HechoDeTexto {
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private Ubicacion ubicacion;
  private Origen origen;
  private InfoContribuyente infoContribuyente;
  private LocalDate fechaHecho; // fecha en que ocurrió el hecho.
  private LocalDateTime fechaDeCarga; // fecha en que se cargó el hecho.
  private LocalDateTime fechaDeBaja; // fecha en que se "eliminó" el hecho.

  @Builder.Default
  private List<Etiqueta> etiquetas = new ArrayList<>();
  @Builder.Default
  private Boolean activo = true; // indica si el hecho está "eliminado" (true) o no (false).

  public Boolean esIgualA(HechoDeTexto hecho) {
    return this.titulo.equalsIgnoreCase(hecho.getTitulo());
  }

  public void agregarEtiqueta(Etiqueta... etiquetas) {
    Collections.addAll(this.etiquetas, etiquetas);
  }
}
