package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Departamento;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Municipio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IDepartamentosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IMunicipiosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IProvinciasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IUbicacionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.INormalizador;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IServicioGeolocalizacion;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class Normalizador implements INormalizador {
  @Autowired
  private ICategoriasRepository categoriasRepository;
  @Autowired
  private IUbicacionesRepository ubicacionesRepository;
  @Autowired
  private IProvinciasRepository provinciasRepository;
  @Autowired
  private IDepartamentosRepository departamentosRepository;
  @Autowired
  private IMunicipiosRepository municipiosRepository;
  @Autowired
  private IServicioGeolocalizacion servicioGeolocalizacion;

  // Locks para evitar duplicados en BD
  private final Object lockCategoria = new Object();
  private final Object lockProvincia = new Object();
  private final Object lockMunicipio = new Object();
  private final Object lockDepartamento = new Object();
  // Uso caffeine para poder ir vaciando la cache de locks
  private final LoadingCache<String, Object> locks = Caffeine.newBuilder()
      .expireAfterAccess(5, TimeUnit.MINUTES)
      .build(key -> new Object());

  private static final Logger log = LoggerFactory.getLogger(Normalizador.class);

  // Normalizo un Hecho completo
  @Override
  public Mono<Hecho> normalizar(Hecho hecho) {
    // Normalizo título
    hecho.setTitulo(normalizarTitulo(hecho.getTitulo()));

    // Normalizo categoría
    hecho.setCategoria(normalizarCategoria(hecho.getCategoria()));

    // Normalizo fechas a LocalDateTime si es que no lo están
    hecho.setFechaHecho(normalizarFecha(hecho.getFechaHecho()));
    hecho.setFechaDeCarga(normalizarFecha(hecho.getFechaDeCarga()));
    hecho.setFechaDeBaja(normalizarFecha(hecho.getFechaDeBaja()));

    // Normalizo Ubicacion
    return normalizarUbicacion(hecho.getUbicacion())
        .map(ubicacionNormalizada -> {
          hecho.setUbicacion(ubicacionNormalizada);
          return hecho;
        });
  }

  @Override
  public String normalizarTitulo(String titulo) {
    // Cambio el titulo a minuscula y le saco los espacios que haya de más.
    return titulo == null ? null : titulo.trim().toLowerCase().replaceAll("\\s+", " ");
  }

  @Override
  public Categoria normalizarCategoria(Categoria categoriaInput) {
    if (categoriaInput == null || categoriaInput.getNombre() == null) return null;
    String nombre = categoriaInput.getNombre().trim();

    synchronized (lockCategoria) {
      return categoriasRepository.findByNombreIgnoreCase(nombre)
          .or(() -> categoriasRepository.findBySinonimoIgnoreCase(nombre))
          .orElseGet(() -> categoriasRepository.save(new Categoria(nombre)));
    }
  }

  @Override
  public LocalDateTime normalizarFecha(LocalDateTime fecha) {
    return fecha; // ya está en LocalDateTime
  }

  @Override
  public LocalDateTime normalizarFecha(LocalDate fecha) {
    return fecha.atStartOfDay(); // convertimos LocalDate a LocalDateTime
  }

  @Override
  public LocalDateTime normalizarFecha(Instant fecha) {
    return fecha.atZone(ZoneOffset.UTC).toLocalDateTime(); // convertimos Instant a LocalDate
  }

  @Override
  public Mono<Ubicacion> normalizarUbicacion(Ubicacion ubicacion) {
    String key = ubicacion.getLatitud() + "-" + ubicacion.getLongitud();
    Object lock = locks.get(key);

    return Mono.fromCallable(() -> {
          synchronized (lock) {
            // 1) Buscar de nuevo porque otro hilo pudo guardarla
            Optional<Ubicacion> existente =
                ubicacionesRepository.findByLatitudAndLongitud(
                    ubicacion.getLatitud(), ubicacion.getLongitud()
                );

            if (existente.isPresent()) {
              return existente.get();
            }

            // 2) Consultar API Externa
            // TODO: Se podria implementar un cron para intentar reparar las ubicaciones incompletas por timeout u otro motivo
            Ubicacion ubic = null;
            try {
              ubic = servicioGeolocalizacion.obtenerUbicacion(
                  ubicacion.getLatitud(), ubicacion.getLongitud()
              ).block();
            } catch (Exception e) {
              log.warn("API Geo falló para [{}, {}]. Se guardará solo lat/long. Error: {}",
                  ubicacion.getLatitud(), ubicacion.getLongitud(), e.getMessage());
            }

            // 3) Decisión de Guardado
            if (ubic == null) {
              // Si la API falló o no devolvió nada => solo guardamos la ubicación original (solo tiene lat/long, el resto null)
              return ubicacionesRepository.save(ubicacion);
            }
            else {
              // Si la API devolvió datos => normalizamos lo que haya venido (mientras no sea null)
              if (ubic.getProvincia() != null) {
                ubic.setProvincia(normalizarProvincia(ubic.getProvincia()));
              }
              if (ubic.getMunicipio() != null) {
                ubic.setMunicipio(normalizarMunicipio(ubic.getMunicipio()));
              }
              if (ubic.getDepartamento() != null) {
                ubic.setDepartamento(normalizarDepartamento(ubic.getDepartamento()));
              }
              return ubicacionesRepository.save(ubic);
            }
          }
        })
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Provincia normalizarProvincia(Provincia provincia) {
    if (provincia == null || provincia.getNombre() == null) return null;
    String nombreProvincia = provincia.getNombre();
    synchronized (lockProvincia) {
      return provinciasRepository.findByNombreIgnoreCase(nombreProvincia)
          .or(() -> provinciasRepository.findBySinonimoIgnoreCase(nombreProvincia))
          .orElseGet(() -> provinciasRepository.save(new Provincia(nombreProvincia)));
    }
  }

  @Override
  public Municipio normalizarMunicipio(Municipio municipio) {
    if (municipio == null || municipio.getNombre() == null) return null;
    String nombreMunicipio = municipio.getNombre();
    synchronized (lockMunicipio) {
      return municipiosRepository.findByNombreIgnoreCase(nombreMunicipio)
          .or(() -> municipiosRepository.findBySinonimoIgnoreCase(nombreMunicipio))
          .orElseGet(() -> municipiosRepository.save(new Municipio(nombreMunicipio)));
    }
  }

  @Override
  public Departamento normalizarDepartamento(Departamento departamento) {
    if (departamento == null || departamento.getNombre() == null) return null;
    String nombreDepartamento = departamento.getNombre();
    synchronized (lockDepartamento) {
      return departamentosRepository.findByNombreIgnoreCase(nombreDepartamento)
          .or(() -> departamentosRepository.findBySinonimoIgnoreCase(nombreDepartamento))
          .orElseGet(() -> departamentosRepository.save(new Departamento(nombreDepartamento)));
    }
  }
}
