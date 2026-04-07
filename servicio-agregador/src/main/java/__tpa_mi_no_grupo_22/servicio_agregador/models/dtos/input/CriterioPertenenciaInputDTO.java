package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.TipoCriterio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CriterioPertenenciaInputDTO {
  private Long id; // Si viene null es porq es un criterio nuevo

  @NotBlank(message = "El nombre del criterio es obligatorio.")
  private String nombreCriterio;

  @NotNull(message = "El tipo de criterio es obligatorio")
  private TipoCriterio tipoCriterio;

  @NotEmpty(message = "Los parámetros son obligatorios.")
  private Map<String, Object> parametros;
}