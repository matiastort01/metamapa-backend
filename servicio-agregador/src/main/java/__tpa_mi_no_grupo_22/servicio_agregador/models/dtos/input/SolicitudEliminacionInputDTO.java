package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SolicitudEliminacionInputDTO {
    @NotNull(message = "La solicitud debe estar asociada a un hecho.")
    private Long idHecho;

    @NotBlank(message = "La justificación es obligatoria.")
    @Size(min = 500, message = "La justificación debe tener por lo menos 500 caracteres.")
    @Size(max = 2000, message = "La justificación no puede superar los 2000 caracteres.")
    private String justificacion;

    private String usuario;
}
