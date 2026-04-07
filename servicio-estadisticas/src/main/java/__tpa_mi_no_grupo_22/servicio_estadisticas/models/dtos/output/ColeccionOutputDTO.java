package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionOutputDTO {
    String titulo;

    String provincia;

    Long cantidad;

    LocalDateTime fechaCreacion;
}
