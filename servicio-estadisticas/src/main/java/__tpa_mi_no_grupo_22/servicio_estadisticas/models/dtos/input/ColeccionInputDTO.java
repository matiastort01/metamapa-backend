package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColeccionInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
}
