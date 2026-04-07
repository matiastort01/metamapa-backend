package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.GlobalBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.SolicitudEliminacionMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudModificacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ISolicitudesEliminacionRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.ISolicitudesEliminacionService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    @Autowired
    private ISolicitudesEliminacionRepository solicitudesRepository;
    @Autowired
    private IHechosRepository hechosRepository;
    @Autowired
    private SolicitudEliminacionMapper solicitudEliminacionMapper;
    @Autowired
    private IHechosService hechosService;
    @Autowired
    private SolicitudesModificacionService solicitudesModificacionService;

    @Override
    public SolicitudEliminacionOutputDTO crearSolicitud(SolicitudEliminacionInputDTO solicitudEliminacionInputDTO) {
        Hecho hecho = this.hechosService.obtenerHechoDominio(solicitudEliminacionInputDTO.getIdHecho());

        SolicitudEliminacion solicitudEliminacion = this.solicitudEliminacionMapper.toSolicitudEliminacion(solicitudEliminacionInputDTO, hecho);

        solicitudEliminacion.setFechaCreacion(LocalDateTime.now());
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

        return this.solicitudEliminacionMapper.toSolicitudEliminacionOutputDTO(solicitudesRepository.save(solicitudEliminacion));
    }

    @Override
    public List<SolicitudEliminacionOutputDTO> obtenerPendientes() {
        // Busca todas las que tengan estado PENDIENTE
        return solicitudesRepository.findByEstadoSolicitud(EstadoSolicitud.PENDIENTE).stream()
            .map(this.solicitudEliminacionMapper::toSolicitudEliminacionOutputDTO)
            .toList();
    }

    @Override
    public List<SolicitudEliminacion> obtenerPendientesPorHecho(Long hechoId) {
        this.hechosService.obtenerHechoDominio(hechoId); // Para verificar que el hecho exista

        return this.solicitudesRepository.findByHechoIdAndEstadoSolicitud(hechoId, EstadoSolicitud.PENDIENTE);
    }

    private SolicitudEliminacion obtenerSolicitudDominio(Long id) {
        return this.solicitudesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("La solicitud de id " + id + " no existe."));
    }

    @Override
    public SolicitudEliminacionOutputDTO obtenerSolicitud(Long id) {
        return this.solicitudEliminacionMapper.toSolicitudEliminacionOutputDTO(this.obtenerSolicitudDominio(id));
    }

    @Override
    @Transactional // Importante: Si falla algo, no se guarda nada
    public void aceptarSolicitud(Long id) {
        // Buscamos la solicitud
        SolicitudEliminacion solicitud = this.obtenerSolicitudDominio(id);

        // Extraemos el hecho
        Hecho hecho = solicitud.getHecho();

        // "Eliminamos" el hecho (Soft Delete)
        hecho.setActivo(false);
        this.hechosRepository.save(hecho);

        // Marcamos la solicitud como ACEPTADA (historial)
        solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);
        solicitud.setFechaModificacion(LocalDateTime.now());

        // Rechazamos el resto de las solicitudes de modificacion y eliminación asociadas al hecho
        List<SolicitudModificacion> solicitudesModificacion = this.solicitudesModificacionService.obtenerPendientesPorHecho(hecho.getId());

        for (SolicitudModificacion solicitudModificacion : solicitudesModificacion) {
            this.solicitudesModificacionService.rechazarSolicitud(solicitudModificacion.getHechoId());
        }

        List<SolicitudEliminacion> solicitudesEliminacion = this.obtenerPendientesPorHecho(hecho.getId());

        for (SolicitudEliminacion solicitudEliminacion : solicitudesEliminacion) {
            this.rechazarSolicitud(solicitudEliminacion.getId());
        }

        this.solicitudesRepository.save(solicitud);
    }

    @Override
    public void rechazarSolicitud(Long id) {
        // 1. Buscamos la solicitud
        SolicitudEliminacion solicitud = this.obtenerSolicitudDominio(id);

        // 2. Solo cambiamos el estado a RECHAZADA (El hecho sigue vivo)
        solicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
        solicitud.setFechaModificacion(LocalDateTime.now());

        this.solicitudesRepository.save(solicitud);
    }
}
