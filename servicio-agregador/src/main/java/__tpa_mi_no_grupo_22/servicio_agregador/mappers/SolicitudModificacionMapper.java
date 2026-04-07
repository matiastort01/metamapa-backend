package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudModificacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudModificacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;
import org.springframework.stereotype.Component;

@Component
public class SolicitudModificacionMapper {
  public SolicitudModificacion toSolicitudModificacion(SolicitudModificacionInputDTO solicitudModificacionInputDTO){
    if(solicitudModificacionInputDTO == null) return null;

    return SolicitudModificacion.builder()
        .hechoId(solicitudModificacionInputDTO.getHechoId())
        .titulo(solicitudModificacionInputDTO.getHecho().getTitulo())
        .descripcion(solicitudModificacionInputDTO.getHecho().getDescripcion())
        .categoria(solicitudModificacionInputDTO.getHecho().getCategoria())
        .latitud(solicitudModificacionInputDTO.getHecho().getLatitud())
        .longitud(solicitudModificacionInputDTO.getHecho().getLongitud())
        .multimedia(solicitudModificacionInputDTO.getHecho().getMultimedia())
        .fechaHecho(solicitudModificacionInputDTO.getHecho().getFecha())
        .usuario(solicitudModificacionInputDTO.getHecho().getUsuario())
        .build();
  }

  public SolicitudModificacionOutputDTO toSolicitudModificacionOutputDTO(SolicitudModificacion solicitudModificacion){
    if(solicitudModificacion == null) return null;

    return SolicitudModificacionOutputDTO.builder()
        .id(solicitudModificacion.getId())
        .hechoId(solicitudModificacion.getHechoId())
        .hechoTitulo(solicitudModificacion.getTitulo())
        .hechoDescripcion(solicitudModificacion.getDescripcion())
        .hechoCategoria(solicitudModificacion.getCategoria())
        .hechoLatitud(solicitudModificacion.getLatitud())
        .hechoLongitud(solicitudModificacion.getLongitud())
        .hechoMultimedia(solicitudModificacion.getMultimedia())
        .fechaHecho(solicitudModificacion.getFechaHecho())
        .estado(solicitudModificacion.getEstadoSolicitud())
        .usuario(solicitudModificacion.getUsuario())
        .build();
  }
}
