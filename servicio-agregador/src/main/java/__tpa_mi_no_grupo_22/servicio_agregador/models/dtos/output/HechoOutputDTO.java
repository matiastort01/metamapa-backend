package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HechoOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> multimedia;
  private List<FuenteOutputDTO> fuentes;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime fechaHecho;
  private UbicacionOutputDTO ubicacionDTO;
  private List<String> etiquetas;
  private String estado;
  private String usuario;
}