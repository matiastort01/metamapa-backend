package __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SoliElimInputDTO {
  @NotNull
  private Long idHecho;

  @NotBlank
  @Size(min = 500)
  private String justificacion;

  @NotBlank
  private String estado;

  public SoliElimInputDTO(Long id, String justificacion, String estado) {
    this.idHecho = id;
    this.justificacion = justificacion;
    this.estado = estado;
  }
}
