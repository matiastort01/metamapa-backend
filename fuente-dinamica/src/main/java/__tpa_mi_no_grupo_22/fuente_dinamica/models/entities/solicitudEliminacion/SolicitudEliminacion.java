package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.solicitudEliminacion;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.solicitudEliminacion.soliEliminacionInfo.EstadoSolicitud;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
@Builder
public class SolicitudEliminacion {
  // ATRIBUTOS
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "hecho_id", referencedColumnName = "id")
  private Hecho hecho;

  @Column(name = "justificacion", nullable = false, columnDefinition = "VARCHAR(2000)")
  private String justificacion;

  @Builder.Default
  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_solicitud")
  private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE;

  // MÉTODOS
  public SolicitudEliminacion(Hecho hecho, String justificacion) {
    if(justificacion == null || justificacion.length() < 500){
      throw new IllegalArgumentException("La justificación debe tener al menos 500 caracteres.");
    }
    this.hecho = hecho;
    this.justificacion = justificacion;
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.fechaCreacion = LocalDateTime.now();
  }

  public void serAceptada(){
    // TODO - REVISAR SPAM
//    if( this.estadoSolicitud == EstadoSolicitud.SPAM ){
//      this.hecho.
//    }
    this.fechaModificacion = LocalDateTime.now();
    this.estadoSolicitud = EstadoSolicitud.ACEPTADA;
    this.hecho.setActivo(false);
  }

  public void serRechazada(){
    this.fechaModificacion = LocalDateTime.now();
    this.estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }
}
