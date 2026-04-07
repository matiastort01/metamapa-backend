package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionInputDTO {
  private String provincia;
  private String municipio;
  private String departamento;

  public UbicacionInputDTO(String provincia, String municipio, String departamento) {
    this.provincia = provincia;
    this.municipio = municipio;
    this.departamento = departamento;
  }
}
