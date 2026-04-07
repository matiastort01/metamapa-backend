package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudModificacionInputDTO {
  @NotNull(message = "La solicitud debe estar asociada a un hecho.")
  private Long hechoId;

  @NotNull(message = "La solicitud debe contener modificaciones del hecho.")
  @Valid
  private HechoFrontInputDTO hecho;
}
