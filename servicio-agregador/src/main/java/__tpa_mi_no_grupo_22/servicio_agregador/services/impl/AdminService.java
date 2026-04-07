package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ActividadDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.DashboardSummaryDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IColeccionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ISolicitudModificacionRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ISolicitudesEliminacionRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

  private final IColeccionesRepository coleccionesRepository;
  private final IHechosRepository hechosRepository;
  private final ISolicitudesEliminacionRepository solicitudesRepository;
  private final ISolicitudModificacionRepository solicitudesModifRepository;

  @Override
  public DashboardSummaryDTO getSummary() {
    long coleccionesCount = coleccionesRepository.count();
    long hechosPendientesCount = hechosRepository.countByEstado(EstadoHecho.PENDIENTE);
    long solicitudesCount = solicitudesRepository.countByEstadoSolicitud(EstadoSolicitud.PENDIENTE);
    long solicitudesModifCount = solicitudesModifRepository.countByEstadoSolicitud(EstadoSolicitud.PENDIENTE);

    return DashboardSummaryDTO.builder()
        .coleccionesActivas(coleccionesCount)
        .hechosPendientes(hechosPendientesCount)
        .solicitudesEliminacion(solicitudesCount)
        .solicitudesModificacion(solicitudesModifCount)
        .build();
  }

  @Override
  public List<ActividadDTO> obtenerActividadReciente() {

    // 1. Obtener últimos 10 HECHOS (Creados)
    List<ActividadDTO> hechos = hechosRepository.findTop10ByOrderByFechaDeCargaDesc().stream()
        .map(h -> ActividadDTO.builder()
            .tipo("NUEVO HECHO")
            .descripcion(h.getTitulo())
            .autor(h.getUsuario() != null ? h.getUsuario() : "Desconocido")
            .fecha(h.getFechaDeCarga())
            .build())
        .toList();

    // 2. Obtener últimas 10 COLECCIONES (Creadas)
    List<ActividadDTO> colecciones = coleccionesRepository.findTop10ByOrderByFechaCreacionDesc().stream()
        .map(c -> ActividadDTO.builder()
            .tipo("NUEVA COLECCIÓN")
            .descripcion(c.getTitulo())
            .autor("ADMIN") // Las colecciones suelen ser de admin
            .fecha(c.getFechaCreacion())
            .build())
        .toList();

    // 3. Obtener últimas 10 SOLICITUDES DE ELIMINACION
    List<ActividadDTO> solicitudes = new ArrayList<>();
    try {
      solicitudes = solicitudesRepository.findTop10ByOrderByFechaCreacionDesc().stream()
          .map(s -> ActividadDTO.builder()
              .tipo("SOLICITUD ELIMINACIÓN")
              .descripcion("Reporte sobre hecho: " + s.getHecho().getTitulo())
              .autor(s.getUsuario() != null ? s.getUsuario() : "Anónimo")
              .fecha(s.getFechaCreacion())
              .build())
          .toList();
    } catch (Exception e) {
      System.err.println("No se pudieron cargar solicitudes: " + e.getMessage());
    }

    // 4. Obtener últimas 10 SOLICITUDES DE MODIFICACION
    List<ActividadDTO> solicitudesModif = new ArrayList<>();
    try {
      solicitudesModif = solicitudesModifRepository.findTop10ByOrderByFechaCreacionDesc().stream()
          .map(s -> ActividadDTO.builder()
              .tipo("SOLICITUD MODIFICACIÓN")
              .descripcion("Reporte sobre hecho: " + s.getHechoId())
              .autor(s.getUsuario() != null ? s.getUsuario() : "Anónimo")
              .fecha(s.getFechaCreacion())
              .build())
          .toList();
    } catch (Exception e) {
      System.err.println("No se pudieron cargar solicitudes: " + e.getMessage());
    }

    // 5. UNIFICAR, ORDENAR Y LIMITAR
    return Stream.of(hechos, colecciones, solicitudes, solicitudesModif)
        .flatMap(List::stream)
        .sorted(Comparator.comparing(ActividadDTO::getFecha).reversed()) // Más nuevo primero
        .limit(40)
        .collect(Collectors.toList());
  }
}


