package __tpa_mi_no_grupo_22.fuente_dinamica.mappers;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Ubicacion;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {
  public Hecho toHecho(HechoInputDTO hechoInputDTO){
    return Hecho.builder()
        .titulo(hechoInputDTO.getTitulo())
        .descripcion(hechoInputDTO.getDescripcion())
        .multimedia(hechoInputDTO.getMultimedia())
        .categoria(new Categoria(hechoInputDTO.getCategoria()))
        .ubicacion(new Ubicacion(hechoInputDTO.getLatitud(), hechoInputDTO.getLongitud()))
        .fechaHecho(hechoInputDTO.getFecha())
        .build();
  }

  public HechoOutputDTO toHechoOutputDTO(Hecho hecho){
    return HechoOutputDTO.builder()
        .titulo(hecho.getTitulo())
        .descripcion(hecho.getDescripcion())
        .categoria(hecho.getCategoria().getNombre())
        .multimedia(hecho.getMultimedia())
        .latitud(hecho.getUbicacion().getLatitud())
        .longitud(hecho.getUbicacion().getLongitud())
        .fecha(hecho.getFechaHecho())
        .fuenteId("FD-1")
        .usuario(hecho.getUsuario())
        .build();
  }
}
