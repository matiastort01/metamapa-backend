package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.FuenteOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import org.springframework.stereotype.Component;

@Component
public class FuenteMapper {
  public FuenteOutputDTO toFuenteOutputDTO(Fuente fuente){
    if (fuente == null) return null; // Validación de seguridad

    return FuenteOutputDTO.builder()
        .id(fuente.getId())
        .nombre(fuente.getNombre())
        .build();
  }
}
