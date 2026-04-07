package __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.output;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DisasterOutputDTO {
  private Long id;

  private String titulo;

  private String descripcion;

  private String categoria;

  private Double latitud;

  private Double longitud;

  private LocalDate fecha;

  private LocalDateTime fechaCarga;

  private String fuenteId;

  private String estado;
}