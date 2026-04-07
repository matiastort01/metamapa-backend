package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;

import java.util.List;

public interface ISolicitudesEliminacionService {

  SolicitudEliminacionOutputDTO crearSolicitud(SolicitudEliminacionInputDTO solicitudEliminacionInputDTO);

  List<SolicitudEliminacionOutputDTO> obtenerPendientes();

  List<SolicitudEliminacion> obtenerPendientesPorHecho(Long hechoId);

  SolicitudEliminacionOutputDTO obtenerSolicitud(Long id);

  void aceptarSolicitud(Long id);

  void rechazarSolicitud(Long id);
}
