package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
  public CategoriaOutputDTO toCategoriaOutputDTO(Categoria categoria){
    if( categoria == null ) return null;

    return CategoriaOutputDTO.builder()
        .id(categoria.getId())
        .nombre(categoria.getNombre())
        .build();
  }
}
