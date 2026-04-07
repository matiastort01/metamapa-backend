package __tpa_mi_no_grupo_22.fuente_estatica.models.entities;

import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Etiqueta;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Origen;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Ubicacion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
@Builder
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "titulo", columnDefinition = "VARCHAR(250)")
  private String titulo;
  @Column(name = "descripcion", columnDefinition = "VARCHAR(250)")
  private String descripcion;
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "categoria_id", referencedColumnName = "id")
  private Categoria categoria;
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
  private Ubicacion ubicacion;
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private Origen origen = Origen.Dataset;
  @Column(name = "archivo_csv")
  private Path archivoCSV;
  @Column(name = "multimedia")
  private Path multimedia;
  @Column(name = "fecha_hecho")
  private LocalDate fechaHecho; // fecha en que ocurrió el hecho.
  @Builder.Default
  @Column(name = "fecha_carga")
  private LocalDateTime fechaDeCarga = LocalDateTime.now(); // fecha en que se cargó el hecho.
  @Column(name = "fecha_baja")
  private LocalDateTime fechaDeBaja; // fecha en que se "eliminó" el hecho.
  @Builder.Default
  @Transient
  private List<Etiqueta> etiquetas = new ArrayList<>();
  @Builder.Default
  @Column(name = "activo", columnDefinition = "BIT(1)")
  private Boolean activo = true; // indica si el hecho está "eliminado" (true) o no (false).
  @Column(name = "estado")

  public Boolean esIgualA(Hecho hecho) {
    return this.titulo.equalsIgnoreCase(hecho.getTitulo());
  }

  public void agregarEtiqueta(Etiqueta... etiquetas) {
    Collections.addAll(this.etiquetas, etiquetas);
  }

  // Agrego esto para detectar 2 hechos iguales si tienen titulos iguales, así el set no añadirá hechos con mismo título.
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Hecho hecho = (Hecho) o;
    return titulo.equalsIgnoreCase(hecho.titulo);
  }

  @Override
  public int hashCode() {
    return titulo.toLowerCase().hashCode();
  }
}
