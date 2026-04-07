package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SolicitudModificacionOutputDTO {
  private Long id;
  private Long hechoId;
  private String hechoTitulo;
  private String hechoDescripcion;
  private String hechoCategoria;
  private BigDecimal hechoLatitud;
  private BigDecimal hechoLongitud;
  private List<String> hechoMultimedia;
  private LocalDateTime fechaHecho;
  private EstadoSolicitud estado;
  private String usuario;
}
