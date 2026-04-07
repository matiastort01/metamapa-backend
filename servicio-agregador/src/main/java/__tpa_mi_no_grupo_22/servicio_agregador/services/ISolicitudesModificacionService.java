package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudModificacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudModificacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;

import java.util.List;

public interface ISolicitudesModificacionService {
  SolicitudModificacionOutputDTO crearSolicitud(SolicitudModificacionInputDTO solicitudModificacionInputDTO);

  List<SolicitudModificacionOutputDTO> obtenerPendientes();

  List<SolicitudModificacion> obtenerPendientesPorHecho(Long hechoId);

  SolicitudModificacionOutputDTO obtenerSolicitud(Long id);

  void aceptarSolicitud(Long id);

  void rechazarSolicitud(Long id);
}
