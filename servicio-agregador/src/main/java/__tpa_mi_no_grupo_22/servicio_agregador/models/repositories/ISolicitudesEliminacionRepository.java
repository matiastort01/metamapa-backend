package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISolicitudesEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {

  // Cuenta la cantidad de hechos que tienen un estado específico.
  long countByEstadoSolicitud(EstadoSolicitud estado);

  List<SolicitudEliminacion> findByEstadoSolicitud(EstadoSolicitud estado);

  List<SolicitudEliminacion> findByHechoIdAndEstadoSolicitud(Long hechoId, EstadoSolicitud estado);

  List<SolicitudEliminacion> findTop10ByOrderByFechaCreacionDesc();
}
