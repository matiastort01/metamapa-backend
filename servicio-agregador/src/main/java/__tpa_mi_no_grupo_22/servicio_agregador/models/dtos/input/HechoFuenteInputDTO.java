package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.utils.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoFuenteInputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> multimedia;
  private BigDecimal latitud;
  private BigDecimal longitud;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime fecha;
  private String fuenteId;
  private String usuario;
}
