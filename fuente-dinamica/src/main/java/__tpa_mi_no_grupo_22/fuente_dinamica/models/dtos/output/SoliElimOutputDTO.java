package __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SoliElimOutputDTO {

  private String tituloHecho;

  private String justificacion;

  private String estado;

  private LocalDateTime fecha;

  public SoliElimOutputDTO(String tituloHecho, String justificacion, String estado, LocalDateTime fecha) {
    this.tituloHecho = tituloHecho;
    this.justificacion = justificacion;
    this.estado = estado;
    this.fecha = fecha;
  }
}
