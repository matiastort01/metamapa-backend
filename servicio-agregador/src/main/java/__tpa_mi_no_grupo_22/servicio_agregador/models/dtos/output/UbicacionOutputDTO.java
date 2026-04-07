package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class UbicacionOutputDTO {
  private String provincia;
  private String municipio;
  private String departamento;
  private BigDecimal latitud;
  private BigDecimal longitud;
}
