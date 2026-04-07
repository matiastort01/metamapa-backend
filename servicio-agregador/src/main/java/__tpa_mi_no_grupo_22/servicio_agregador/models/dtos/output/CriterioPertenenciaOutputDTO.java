package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.TipoCriterio;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CriterioPertenenciaOutputDTO {
  private Long id;
  private String nombreCriterio;
  private TipoCriterio tipoCriterio;
  private Map<String, Object> parametros;
}
