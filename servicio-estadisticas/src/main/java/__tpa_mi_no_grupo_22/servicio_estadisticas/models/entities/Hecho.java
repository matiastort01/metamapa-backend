package __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Hecho {
  private String titulo;
  private String categoria;
  private LocalDateTime fechaHecho;
  private String provincia;
}
