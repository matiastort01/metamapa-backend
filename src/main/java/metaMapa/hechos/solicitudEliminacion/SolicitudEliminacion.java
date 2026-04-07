package metaMapa.hechos.solicitudEliminacion;

import lombok.Data;
import metaMapa.hechos.HechoDeTexto;
import org.mockito.internal.matchers.Null;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminacion {
  private HechoDeTexto hecho;
  private String justificacion;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaModificacion;
  private EstadoSolicitud estadoSolicitud;

  public SolicitudEliminacion(HechoDeTexto hecho, String justificacion) {
    if( justificacion == null || justificacion.length() < 500 ){
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
