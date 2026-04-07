package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.GlobalBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.SolicitudModificacionMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFrontInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudModificacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudModificacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.EstadoSolicitud;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes.SolicitudModificacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ISolicitudModificacionRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.ISolicitudesModificacionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SolicitudesModificacionService implements ISolicitudesModificacionService {
  @Autowired
  private ISolicitudModificacionRepository solicitudesRepository;
  @Autowired
  private IHechosRepository hechosRepository;
  @Autowired
  private IHechosService hechosService;
  @Autowired
  private SolicitudModificacionMapper solicitudModificacionMapper;

  @Override
  public SolicitudModificacionOutputDTO crearSolicitud(SolicitudModificacionInputDTO solicitudModificacionInputDTO) {
    // Busco el hecho original
    Hecho hechoOriginal = hechosRepository.findById(solicitudModificacionInputDTO.getHechoId())
        .orElseThrow(() -> new ResourceNotFoundException("El hecho de id " + solicitudModificacionInputDTO.getHechoId() + " no existe."));

    // VALIDACIONES
    Map<String, String> errores = new HashMap<>();

    // --- VALIDACIÓN DE 7 DÍAS ---
    // Calculamos la diferencia entre la fecha de carga original y hoy
    LocalDate fechaCarga = hechoOriginal.getFechaDeCarga().toLocalDate();
    long diasTranscurridos = ChronoUnit.DAYS.between(fechaCarga, LocalDate.now());

    if (Math.abs(diasTranscurridos) > 7) {
      throw new GlobalBusinessException(
          "CONFLICT",
          "El plazo de modificación ha expirado.",
          List.of("Han pasado " + Math.abs(diasTranscurridos) + " días desde la carga. El máximo permitido es 7 días.")
      );
    }

    // Validamos que no haya una PENDIENTE (pero sí permitimos si hay RECHAZADAS previas)
    boolean yaTienePendiente = solicitudesRepository.existsByHechoIdAndUsuarioAndEstadoSolicitud(
        solicitudModificacionInputDTO.getHechoId(),
        solicitudModificacionInputDTO.getHecho().getUsuario(),
        EstadoSolicitud.PENDIENTE
    );

    if (yaTienePendiente) {
      throw new GlobalBusinessException(
          "CONFLICT",
          "Ya tienes una solicitud pendiente para este hecho.",
          List.of("Debes esperar a que se resuelva tu solicitud anterior antes de enviar otra.")
      );
    }

    // --- TÍTULO DUPLICADO ---
    // Solo validamos si el usuario intenta cambiar el título
    if (solicitudModificacionInputDTO.getHecho().getTitulo() != null && !solicitudModificacionInputDTO.getHecho().getTitulo().trim().equalsIgnoreCase(hechoOriginal.getTitulo())) {
      // Verificamos si el nuevo título ya existe en CUALQUIER OTRO hecho (excluyendo el actual)
      if (hechosRepository.existsByTituloAndIdNot(solicitudModificacionInputDTO.getHecho().getTitulo(), hechoOriginal.getId())) {
        errores.put("titulo", "El título '" + solicitudModificacionInputDTO.getHecho().getTitulo() + "' ya pertenece a otro hecho.");
      }
    }

    if (!errores.isEmpty()) {
      throw new ValidationBusinessException(errores);
    }

    // Si pasa todas las validaciones creamos la solicitud
    SolicitudModificacion solicitudModificacion = this.solicitudModificacionMapper.toSolicitudModificacion(solicitudModificacionInputDTO);

    // Completamos datos de auditoría y estado
    solicitudModificacion.setFechaCreacion(LocalDateTime.now());
    solicitudModificacion.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

    // Guardamos y retornamos
    return this.solicitudModificacionMapper.toSolicitudModificacionOutputDTO(solicitudesRepository.save(solicitudModificacion));
  }

  @Override
  public List<SolicitudModificacionOutputDTO> obtenerPendientes() {
    // Busca todas las que tengan estado PENDIENTE
    return this.solicitudesRepository.findByEstadoSolicitud(EstadoSolicitud.PENDIENTE).stream().map(this.solicitudModificacionMapper::toSolicitudModificacionOutputDTO).toList();
  }

  @Override
  public List<SolicitudModificacion> obtenerPendientesPorHecho(Long hechoId) {
    this.hechosService.obtenerHechoDominio(hechoId); // Para verificar que el hecho exista

    return this.solicitudesRepository.findByHechoIdAndEstadoSolicitud(hechoId, EstadoSolicitud.PENDIENTE);
  }

  private SolicitudModificacion obtenerSolicitudDominio(Long id) {
    return this.solicitudesRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("La solicitud de id " + id + " no existe."));
  }

  @Override
  public SolicitudModificacionOutputDTO obtenerSolicitud(Long id) {
    return this.solicitudModificacionMapper.toSolicitudModificacionOutputDTO(this.obtenerSolicitudDominio(id));
  }

  @Override
  @Transactional
  public void aceptarSolicitud(Long id) {
    // Buscamos la solicitud
    SolicitudModificacion solicitud = this.obtenerSolicitudDominio(id);

    // Creamos el hecho input para enviarle a editar hecho
    HechoFrontInputDTO hechoInputDTO = HechoFrontInputDTO.builder()
        .titulo(solicitud.getTitulo())
        .descripcion(solicitud.getDescripcion())
        .categoria(solicitud.getCategoria())
        .multimedia(solicitud.getMultimedia() != null
            ? new ArrayList<>(solicitud.getMultimedia())
            : new ArrayList<>())
        .latitud(solicitud.getLatitud())
        .longitud(solicitud.getLongitud())
        .fecha(solicitud.getFechaHecho())
        .usuario(solicitud.getUsuario())
        .build();

    // Editamos el hecho
    this.hechosService.editarHecho(solicitud.getHechoId(), hechoInputDTO);

    // Marcamos la solicitud como ACEPTADA y la almacenamos
    solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);
    solicitud.setFechaModificacion(LocalDateTime.now());

    this.solicitudesRepository.save(solicitud);
  }

  @Override
  public void rechazarSolicitud(Long id) {
    // Buscamos la solicitud
    SolicitudModificacion solicitud = this.obtenerSolicitudDominio(id);

    // Solo cambiamos el estado a RECHAZADA (El hecho sigue vivo)
    solicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
    solicitud.setFechaModificacion(LocalDateTime.now());

    this.solicitudesRepository.save(solicitud);
  }
}
