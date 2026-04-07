package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ISolicitudModificacionRepository extends JpaRepository<SolicitudModificacion, Long> {
  boolean existsByHechoIdAndUsuarioAndEstadoSolicitud(Long hechoId, String usuario, EstadoSolicitud estado);

  long countByEstadoSolicitud(EstadoSolicitud estado);

  List<SolicitudModificacion> findByEstadoSolicitud(EstadoSolicitud estado);

  List<SolicitudModificacion> findByHechoIdAndEstadoSolicitud(Long hechoId, EstadoSolicitud estado);

  List<SolicitudModificacion> findTop10ByOrderByFechaCreacionDesc();
}
