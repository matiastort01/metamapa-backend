package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos;


import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

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

  @Column(name = "titulo", columnDefinition = "VARCHAR(250)", unique = true, nullable = false)
  private String titulo;


  @Column(name = "descripcion", columnDefinition = "VARCHAR(2000)")
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "categoria_id", referencedColumnName = "id")
  private Categoria categoria;

  @ManyToOne
  @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
  private Ubicacion ubicacion;

  @Enumerated(EnumType.STRING)
  private Origen origen;

  @ElementCollection
  @CollectionTable(name = "hecho_multimedias", joinColumns = @JoinColumn(name = "hecho_id"))
  @Column(name = "multimedia", columnDefinition = "VARCHAR(250)")
  @BatchSize(size = 50)
  private List<String> multimedia;

  @Column(name = "fecha_hecho")
  private LocalDateTime fechaHecho; // fecha en que ocurrió el hecho.

  @Builder.Default
  @Column(name = "fecha_carga")
  private LocalDateTime fechaDeCarga = LocalDateTime.now(); // fecha en que se cargó el hecho.

  @Column(name = "fecha_baja")
  private LocalDateTime fechaDeBaja; // fecha en que se "eliminó" el hecho.

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "hecho_fuente",
      joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  )
  @BatchSize(size = 50)
  private Set<Fuente> fuentes = new HashSet<>();

  @Builder.Default
  @Transient
  private List<Etiqueta> etiquetas = new ArrayList<>();

  @Column(name = "activo", columnDefinition = "BIT(1)")
  private Boolean activo; // indica si el hecho está "eliminado" (true) o no (false).

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private EstadoHecho estado;

  @Column(name = "usuario")
  private String usuario;

  public Boolean esIgualA(Hecho hecho) {
    return this.titulo.equalsIgnoreCase(hecho.getTitulo());
  }

  public void agregarEtiqueta(Etiqueta... etiquetas) {
    Collections.addAll(this.etiquetas, etiquetas);
  }
}