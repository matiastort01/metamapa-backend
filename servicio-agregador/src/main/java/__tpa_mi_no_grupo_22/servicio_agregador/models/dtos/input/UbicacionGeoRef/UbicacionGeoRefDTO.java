package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.UbicacionGeoRef;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UbicacionGeoRefDTO {
  DepartamentoGeoRefDTO departamento;
  BigDecimal lat;
  BigDecimal lon;
  MunicipioGeoRefDTO municipio;
  ProvinciaGeoRefDTO provincia;
}
