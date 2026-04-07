package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColeccionOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private List<CriterioPertenenciaOutputDTO> criteriosDePertenencias;
    private List<FuenteOutputDTO> fuentes;
    private TipoAlgoritmoConsenso algoritmoConsenso;
}

