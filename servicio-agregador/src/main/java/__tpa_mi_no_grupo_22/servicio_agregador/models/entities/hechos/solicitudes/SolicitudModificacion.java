package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes;


import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Origen;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_modificacion")
@Builder
public class SolicitudModificacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hecho_id")
  private Long hechoId;

  @Column(name = "titulo", columnDefinition = "VARCHAR(250)", nullable = false)
  private String titulo;

  @Column(name = "descripcion", columnDefinition = "VARCHAR(2000)")
  private String descripcion;

  @Column(name = "categoria", columnDefinition = "VARCHAR(100)")
  private String categoria;

  @Column(name = "latitud",  nullable = false, precision = 12, scale = 7)
  private BigDecimal latitud;

  @Column(name = "longitud", nullable = false, precision = 12, scale = 7)
  private BigDecimal longitud;

  @Column(name = "multimedia", columnDefinition = "VARCHAR(250)")
  @ElementCollection
  @CollectionTable(name = "solicitud_hecho_multimedias", joinColumns = @JoinColumn(name = "solicitud_id"))
  private List<String> multimedia;

  @Column(name = "fecha_hecho")
  private LocalDateTime fechaHecho; // fecha en que ocurrió el hecho.

  @Column(name = "usuario")
  private String usuario;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_solicitud")
  private EstadoSolicitud estadoSolicitud;
}
