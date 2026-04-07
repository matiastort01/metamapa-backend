package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudEliminacion;
import org.springframework.stereotype.Component;

@Component
public class SolicitudEliminacionMapper {
  public SolicitudEliminacion toSolicitudEliminacion(SolicitudEliminacionInputDTO solicitudEliminacionInputDTO, Hecho hecho){
    return SolicitudEliminacion.builder()
        .hecho(hecho)
        .justificacion(solicitudEliminacionInputDTO.getJustificacion())
        .usuario(solicitudEliminacionInputDTO.getUsuario())
        .build();
  }

  public SolicitudEliminacionOutputDTO toSolicitudEliminacionOutputDTO(SolicitudEliminacion solicitudEliminacion){
    return SolicitudEliminacionOutputDTO.builder()
        .id(solicitudEliminacion.getId())
        .hechoTitulo(solicitudEliminacion.getHecho().getTitulo())
        .idHecho(solicitudEliminacion.getHecho().getId())
        .justificacion(solicitudEliminacion.getJustificacion())
        .estado(solicitudEliminacion.getEstadoSolicitud())
        .usuario(solicitudEliminacion.getUsuario())
        .build();
  }
}
