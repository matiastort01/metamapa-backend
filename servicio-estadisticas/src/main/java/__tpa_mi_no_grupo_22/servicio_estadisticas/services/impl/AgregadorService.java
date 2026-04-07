package __tpa_mi_no_grupo_22.servicio_estadisticas.services.impl;

import __tpa_mi_no_grupo_22.servicio_estadisticas.clients.AgregadorClient;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.*;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.repositories.*;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.utils.ColeccionMapper;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.utils.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_estadisticas.services.IAgregadorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgregadorService implements IAgregadorService {

  private static final Logger log = LoggerFactory.getLogger(AgregadorService.class);
  @Autowired
  private  AgregadorClient client;
  @Autowired
  private  HechoMapper hechoMapper;
  @Autowired
  private  ColeccionMapper coleccionMapper;
  @Autowired
  private  IProvinciaColeccionRepository coleccionRepo;
  @Autowired
  private ICategoriasRepository categoriasRepository;
//le pedi a gepeto un refactor


  @Override
  public void recalcular() {
    log.info("🔄 Iniciando recalculo de estadísticas...");

    List<Hecho> hechos = obtenerHechos();
    if (hechos.isEmpty()) {
      log.warn("⚠️ No se obtuvieron hechos. Cancelando cálculo.");
      return;
    }

    List<Coleccion> colecciones = obtenerColecciones();
    log.info("📊 Hechos obtenidos: {} | Colecciones: {}", hechos.size(), colecciones.size());

    calcularEstadisticasGenerales(hechos);
    calcularEstadisticasPorColeccion(colecciones);
    log.info("✅ Recalculo finalizado con éxito");
  }

  // =============================================================
  // =============== CALCULOS PRINCIPALES =========================
  // =============================================================

  private void calcularEstadisticasGenerales(List<Hecho> hechos) {
    log.info("📈 Calculando estadísticas generales...");
    guardarCategorias(hechos);
  }

  private void calcularEstadisticasPorColeccion(List<Coleccion> colecciones) {
    log.info("📦 Calculando estadísticas por colección...");
    for (Coleccion c : colecciones) {
      List<Hecho> hechosColeccion = obtenerHechosPorColeccion(c.getId());
      if (!hechosColeccion.isEmpty()) {
        guardarProvinciaMasFrecuenteDeColeccion(c, hechosColeccion);
      } else {
        log.warn("Colección '{}' no tiene hechos asociados", c.getTitulo());
      }
    }
  }

  // =============================================================
  // ================= OBTENCIÓN DE DATOS =========================
  // =============================================================

  private List<Hecho> obtenerHechos() {
    return client.obtenerHechos().stream()
            .map(hechoMapper::hechoInputDTOToHecho)
            .toList();
  }

  private List<Coleccion> obtenerColecciones() {
    return client.obtenerColecciones().stream()
            .map(coleccionMapper::ColeccionInputDTOToColeccion)
            .toList();
  }

  private List<Hecho> obtenerHechosPorColeccion(Long id) {
    return client.obtenerHechosColeccion(id).stream()
            .map(hechoMapper::hechoInputDTOToHecho)
            .toList();
  }

  // =============================================================
  // ================= CÁLCULOS Y GUARDADO ========================
  // =============================================================
  private void guardarCategorias(List<Hecho> hechos) {
        log.info("Hechos dentro de guardar categorias: "+ hechos.size());
        log.info("Primer hecho categoria:{} provincia:{} fecha:{}",hechos.get(0).getCategoria(), hechos.get(0).getProvincia(), hechos.get(0).getFechaHecho());
      // 1) Filtrar hechos inválidos
      List<Hecho> hechosValidos = hechos.stream()
              .filter(h -> h.getCategoria() != null && !h.getCategoria().isBlank())
              .filter(h -> h.getProvincia() != null && !h.getProvincia().isBlank())
              .filter(h -> h.getFechaHecho() != null)
              .toList();
      log.info("Hechos filtrados: "+ hechosValidos.size());
      // 2) Agrupar por categoría (todas válidas)
      Map<String, List<Hecho>> hechosPorCategoria =
              hechosValidos.stream()
                      .collect(Collectors.groupingBy(Hecho::getCategoria));
      log.info("Filtro 2: "+ hechosPorCategoria.size());
      List<CategoriaEstadistica> estadisticasAGuardar = new ArrayList<>();

      for (Map.Entry<String, List<Hecho>> entry : hechosPorCategoria.entrySet()) {

          String categoria = entry.getKey();
          List<Hecho> listaHechos = entry.getValue();

          Long cantidad = (long) listaHechos.size();

          // 3) Provincia más frecuente (todas válidas)
          String provinciaMasFrecuente = listaHechos.stream()
                  .collect(Collectors.groupingBy(
                          Hecho::getProvincia,
                          Collectors.counting()
                  ))
                  .entrySet()
                  .stream()
                  .max(Map.Entry.comparingByValue())
                  .map(Map.Entry::getKey)
                  .orElse(null);

          // 4) Hora más frecuente (todas válidas)
          Integer horaMasFrecuente = listaHechos.stream()
                  .collect(Collectors.groupingBy(
                          h -> h.getFechaHecho().getHour(),
                          Collectors.counting()
                  ))
                  .entrySet()
                  .stream()
                  .max(Map.Entry.comparingByValue())
                  .map(Map.Entry::getKey)
                  .orElse(null);

          CategoriaEstadistica estadistica =
                  new CategoriaEstadistica(categoria, cantidad, provinciaMasFrecuente, horaMasFrecuente);
            log.info("Estadistica a guardar:cantidad: {} provincia:  {} nombre: {}  hora: {}",estadistica.getCantidad(),estadistica.getProvincia(),estadistica.getNombre(),estadistica.getHora());
          estadisticasAGuardar.add(estadistica);
      }

      categoriasRepository.saveAll(estadisticasAGuardar);
  }



  private void guardarProvinciaMasFrecuenteDeColeccion(Coleccion coleccion, List<Hecho> hechos) {
    hechos.stream()
            .filter(h -> h.getProvincia() != null)
            .collect(Collectors.groupingBy(Hecho::getProvincia, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> {
              coleccionRepo.save(new ProvinciaColeccionEstadistica(coleccion.getTitulo(), entry.getKey(), entry.getValue()));
              log.info("📍 Colección '{}' -> Provincia más frecuente: {} ({} hechos)",
                      coleccion.getTitulo(), entry.getKey(), entry.getValue());
            });
  }

}
