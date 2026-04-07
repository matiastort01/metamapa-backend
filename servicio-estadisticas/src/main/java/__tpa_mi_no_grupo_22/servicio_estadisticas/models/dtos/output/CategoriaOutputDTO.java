package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaOutputDTO {
    String categoria;
    Long cantidad;
    String provincia;
    Integer hora;
    LocalDateTime fecha;
}
