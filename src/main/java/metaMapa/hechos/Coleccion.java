package metaMapa.hechos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import metaMapa.hechos.criteriosDePertenencia.ICriterioDePertenencia;

public class Coleccion {
  @Getter
  @Setter
  private String titulo;
  @Getter
  @Setter
  private String descripcion;
  @Getter
  @Setter
  private List<HechoDeTexto> hechos;
  @Getter
  private ICriterioDePertenencia criterio;
  @Getter
  @Setter
  private LocalDateTime fechaCreacion;

  public Coleccion(String titulo, String descripcion, ICriterioDePertenencia criterio, HechoDeTexto... hechos) {
//    if (criterio == null) {
//      throw new IllegalArgumentException("El criterio de pertenencia no puede ser null.");
//    }

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criterio = criterio;
    this.fechaCreacion = LocalDateTime.now(); // Se asigna la fecha actual
    this.hechos = new ArrayList<>();
    this.agregarHechos(hechos);
  }

  public void agregarHechos(HechoDeTexto... hechos) {
    for (HechoDeTexto hecho : hechos) {
      if (hecho != null) {
        boolean estaActivo = hecho.getActivo();
        boolean cumpleCriterio = this.criterio == null || this.criterio.perteneceA(hecho);

        if (estaActivo && cumpleCriterio) {
          // Si ya existe un hecho con el mismo titulo lo borra, dado que se debe pisar la info con el nuevo
          this.hechos.removeIf(h -> h.getTitulo().equals(hecho.getTitulo()));

          // Agrego el nuevo hecho
          this.hechos.add(hecho);
        }
      }
    }
  }

  public void setCriterio(ICriterioDePertenencia criterio) {
    this.criterio = criterio;
    this.hechos = this.hechos.stream().filter(criterio::perteneceA).collect(Collectors.toList());
  }

}
