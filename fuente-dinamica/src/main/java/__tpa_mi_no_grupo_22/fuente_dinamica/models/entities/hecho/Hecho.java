package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.converter.PathAtributteConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Etiqueta;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.InfoContribuyente;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Origen;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
@Builder
public class Hecho {
  // ATRIBUTOS
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", columnDefinition = "VARCHAR(100)", unique = true)
  private String titulo;

  @Column(name = "descripcion", columnDefinition = "VARCHAR(500)")
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "categoria_id", referencedColumnName = "id")
  private Categoria categoria;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
  private Ubicacion ubicacion;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private Origen origen = Origen.Dataset;

//  @ManyToOne(cascade = CascadeType.PERSIST)
  @ElementCollection
  @CollectionTable(name = "hecho_multimedias", joinColumns = @JoinColumn(name = "hecho_id"))
  @Column(name = "multimedia", columnDefinition = "VARCHAR(250)")
  private List<String> multimedia;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "contribuyente") // Omito el "referencedColumnName = id" porque ManyToOne por defecto apunta a la PK de la entidad referenciada
  private InfoContribuyente infoContribuyente;

  @Column(name = "usuario")
  private String usuario; // Para guardar el email del que lo subió (ej: visualizador o admin)

  @Column(name = "fecha_hecho")
  private LocalDateTime fechaHecho; // fecha en que ocurrió el hecho.

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

  // MÉTODOS
  public Boolean esIgualA(Hecho hecho) {
    return this.titulo.equalsIgnoreCase(hecho.getTitulo());
  }

  public void agregarEtiqueta(Etiqueta... etiquetas) {
    Collections.addAll(this.etiquetas, etiquetas);
  }
}
