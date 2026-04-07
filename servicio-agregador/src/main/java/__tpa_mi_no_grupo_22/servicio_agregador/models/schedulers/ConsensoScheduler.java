package __tpa_mi_no_grupo_22.servicio_agregador.models.schedulers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Coleccion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.EstadoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.IAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IColeccionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IEstadoConsensoRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConsensoScheduler {

  @Autowired
  private IHechosRepository hechosRepository;

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Autowired
  private IEstadoConsensoRepository estadoConsensoRepository;

  @Autowired
  private List<IAlgoritmoConsenso> algoritmosConsenso;

  private static final Logger log = LoggerFactory.getLogger(ConsensoScheduler.class);

//  @Scheduled(cron = "0 0 3 * * *") // Todos los días a las 3 AM -> 0 0 3 * * *
//  public void evaluarConsensos() {
//    log.info("Iniciando scheduler de consensos...");
//
//    List<Hecho> hechosActivos = hechosRepository.findAllByActivoIsTrueAndEstado(EstadoHecho.ACEPTADO);
//
//    List<Coleccion> colecciones = coleccionesRepository.findAll();
//
//    Map<TipoAlgoritmoConsenso, IAlgoritmoConsenso> mapAlgoritmosConsenso = algoritmosConsenso.stream()
//        .collect(Collectors.toMap(IAlgoritmoConsenso::getTipo, Function.identity()));
//
//    for( Coleccion coleccion : colecciones ){
//      TipoAlgoritmoConsenso tipoAlgoritmoConsenso = coleccion.getTipoAlgoritmoConsenso();
//      if( tipoAlgoritmoConsenso == null ){
//        // Si la coleccion no tiene algoritmo definido salto a la siguiente
//        continue;
//      }
//
//      IAlgoritmoConsenso algoritmoConsenso = mapAlgoritmosConsenso.get(tipoAlgoritmoConsenso);
//
//      List<String> fuentesColeccion = coleccion.getFuentes(); // TODO: CAMBIAR IMPLEMENTACION DE COLECCION A SET DE FUENTES
//      int totalFuentes = fuentesColeccion.size();
//      String tituloColeccion = coleccion.getTitulo();
//
//      // Filtrar hechos de las fuentes de esta colección
//      List<Hecho> hechosFiltrados = hechosActivos.stream()
//          .filter(hecho -> fuentesColeccion.contains(hecho.getFuente()))
//          .toList();
//
//      // Agrupar hechos por título
//      Map<String, List<Hecho>> hechosAgrupados = hechosFiltrados.stream()
//          .collect(Collectors.groupingBy(Hecho::getTitulo));
//
//      hechosAgrupados.forEach((tituloHecho, hechosConMismoTitulo) -> {
//        // Obtener la cantidad de fuentes distintas que mencionaron el hecho
//        int cantidadMenciones = (int) hechosConMismoTitulo.stream()
//            .map(Hecho::getFuente)
//            .distinct()
//            .count();
//
//        boolean consensuado = algoritmoConsenso.esConsensuado(cantidadMenciones, totalFuentes);
//
//        EstadoConsenso estado = estadoConsensoRepository.findById(tituloHecho, tituloColeccion, tipoAlgoritmoConsenso);
//
//        if( estado == null ){
//          estado = EstadoConsenso.builder()
//              .tituloHecho(tituloHecho)
//              .tituloColeccion(tituloColeccion)
//              .tipoAlgoritmoConsenso(tipoAlgoritmoConsenso)
//              .consensuado(consensuado)
//              .build();
//        }
//        else{
//          estado.setConsensuado(consensuado);
//        }
//
//        estadoConsensoRepository.save(estado);
//      });
//    }
//
//    log.info("Scheduler de consensos finalizado.");
//  }

  @Scheduled(cron = "0 0 3 * * *")
  @Transactional
  public void evaluarConsensos() {
    log.info("Iniciando scheduler de consensos...");

    // Obtengo las colecciones
    List<Coleccion> colecciones = coleccionesRepository.findAllConFuentes();

    // Cache de tipoAlgoritmo y algoritmo para acceder mas fácil luego
    Map<TipoAlgoritmoConsenso, IAlgoritmoConsenso> mapAlgoritmos = algoritmosConsenso.stream()
        .collect(Collectors.toMap(IAlgoritmoConsenso::getTipo, Function.identity()));

    // Recorro coleccion a coleccion para comprobar si los hechos asociados estan consensuados en funcion de la cantidad de fuentes y el tipo de algoritmo
    for (Coleccion coleccion : colecciones) {
      TipoAlgoritmoConsenso tipoAlgoritmoConsenso = coleccion.getTipoAlgoritmoConsenso();
      if (tipoAlgoritmoConsenso == null) {
        // Si la coleccion no tiene algoritmo definido salto a la siguiente
        continue;
      }

      // Obtengo las fuentes de la colección
      Set<Fuente> fuentesColeccion = coleccion.getFuentes();

      // Si la colección no tiene fuentes, no buscamos nada (no deberia suceder nunca que no tenga pero bueno)
      if (fuentesColeccion.isEmpty()) continue;

      // Vamos a la BD solo por lo que nos sirve: "Dame hechos aceptados que tengan AL MENOS UNA de estas fuentes"
      List<Hecho> hechosRelevantes = hechosRepository.findDistinctByActivoTrueAndEstadoAndFuentes_IdIn(
          EstadoHecho.ACEPTADO,
          fuentesColeccion.stream().map(Fuente::getId).toList()
      );

      IAlgoritmoConsenso algoritmo = mapAlgoritmos.get(tipoAlgoritmoConsenso);

      // Itero los hechos y corrobor si estan consensuados para la coleccion
      for (Hecho hecho : hechosRelevantes) {
        // Calculamos cuántas fuentes del hecho coinciden con las de la colección
        long cantidadMenciones = hecho.getFuentes().stream()
            .filter(fuentesColeccion::contains)
            .count();

        boolean consensuado = algoritmo.esConsensuado((int) cantidadMenciones, fuentesColeccion.size());

        // Guardamos el nuevo estado
        guardarEstadoConsenso(hecho, coleccion, tipoAlgoritmoConsenso, consensuado);
      }
    }

    log.info("Scheduler de consenso finalizado.");
  }

  private void guardarEstadoConsenso(Hecho hecho, Coleccion coleccion, TipoAlgoritmoConsenso tipo, boolean nuevoValorConsenso) {
    // Buscamos si ya existe un registro de estado para esta combinación
    Optional<EstadoConsenso> estadoOpt = estadoConsensoRepository
        .findByTituloHechoAndTituloColeccionAndTipoAlgoritmoConsenso(
            hecho.getTitulo(), coleccion.getTitulo(), tipo
        );

    EstadoConsenso estado;
    if (estadoOpt.isPresent()) {
      // Actualizo el estado solo si cambió el valor
      estado = estadoOpt.get();
      if (estado.isConsensuado() == nuevoValorConsenso) {
        // Si el estado no cambio salgo, ya que no necesito persistir ningun cambio
        return;
      }
      // Si el estado cambio lo seteo para luego persistirlo
      estado.setConsensuado(nuevoValorConsenso);
    }
    else {
      // Creo un estado consenso nuevo
      estado = EstadoConsenso.builder()
          .tituloHecho(hecho.getTitulo())
          .tituloColeccion(coleccion.getTitulo())
          .tipoAlgoritmoConsenso(tipo)
          .consensuado(nuevoValorConsenso)
          .build();
    }

    // Persisto el nuevo estado
    estadoConsensoRepository.save(estado);
  }
}

