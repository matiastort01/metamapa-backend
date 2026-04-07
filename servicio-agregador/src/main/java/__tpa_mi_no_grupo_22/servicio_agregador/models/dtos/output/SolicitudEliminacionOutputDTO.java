package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitudEliminacionOutputDTO {
  private Long id;
  private Long idHecho;
  private String hechoTitulo;
  private String justificacion;
  private EstadoSolicitud estado;
  private String usuario;
}
