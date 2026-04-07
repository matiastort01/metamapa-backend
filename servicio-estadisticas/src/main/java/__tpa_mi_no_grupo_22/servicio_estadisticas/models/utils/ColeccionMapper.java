package __tpa_mi_no_grupo_22.servicio_estadisticas.models.utils;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.Coleccion;
import org.springframework.stereotype.Component;

@Component
public class ColeccionMapper {


    public Coleccion ColeccionInputDTOToColeccion(ColeccionInputDTO dto){

        return Coleccion.builder()
                        .descripcion(dto.getDescripcion())
                        .titulo(dto.getTitulo())
                        .id(dto.getId())
                        .build();
    }
}
