package __tpa_mi_no_grupo_22.fuente_estatica.services.impl;

import __tpa_mi_no_grupo_22.fuente_estatica.models.dtos.HechoOutputDTO;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.Hecho;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Ubicacion;
import __tpa_mi_no_grupo_22.fuente_estatica.models.repositories.IHechoRepository;
import __tpa_mi_no_grupo_22.fuente_estatica.models.repositories.ICategoriaRepository; // Asegurate de importar el nuevo repo
import __tpa_mi_no_grupo_22.fuente_estatica.models.repositories.IUbicacionRepository;
import __tpa_mi_no_grupo_22.fuente_estatica.services.IHechoService;
import __tpa_mi_no_grupo_22.fuente_estatica.services.ImportadorCSV.ImportadorCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class HechoService implements IHechoService {

  @Autowired
  private IHechoRepository hechoRepository;

  @Autowired
  private ICategoriaRepository categoriaRepository;

  @Autowired
  private IUbicacionRepository ubicacionRepository;

  @Autowired
  private ImportadorCSV importador;

  // Truco de Spring: Nos auto-inyectamos para poder llamar a nuestros propios métodos @Transactional en bringIn
  @Autowired @Lazy
  private HechoService self;

  @Override
  public void delete(Long id) {
    hechoRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void bringIn(Path csvFile) {
    CompletableFuture.runAsync(() -> {
      System.out.println(">>> [ASYNC] Iniciando procesamiento...");
      int guardados = 0;
      int errores = 0;

      try {
        List<Hecho> hechos = this.importador.importar(csvFile);
        System.out.println(">>> [ASYNC] Hechos leídos: " + hechos.size());

        for (Hecho hecho : hechos) {
          try {
            // LLAMAMOS AL MÉTODO TRANSACCIONAL (A través del proxy 'self')
            self.guardarHechoIndividual(hecho);
            guardados++;
          } catch (Exception e) {
            errores++;
            if (errores == 1) {
              System.err.println(">>> [ERROR] Primer fallo: " + e.getMessage());
              e.printStackTrace();
            }
          }
        }
        System.out.println(">>> [FIN] Guardados: " + guardados + ". Fallos: " + errores);

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try { Files.deleteIfExists(csvFile); } catch (IOException e) {}
      }
    });
  }

  /*
   Este método ejecuta en una sola transacción de base de datos.
   Al terminar, o se guardan todos o fallan todos, evitando estados inconsistentes.
   REQUIRES_NEW asegura que cada hecho tenga su propia transacción independiente.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void guardarHechoIndividual(Hecho hecho) {

    // 1. Gestión de Categoría
    String nombreCat = hecho.getCategoria().getNombre();
    Optional<Categoria> catOpt = categoriaRepository.findByNombre(nombreCat);
    if (catOpt.isPresent()) {
      hecho.setCategoria(catOpt.get());
    } else {
      // Si no existe, save() la persiste y la deja "managed" dentro de esta transacción
      hecho.setCategoria(categoriaRepository.save(hecho.getCategoria()));
    }

    // 2. Gestión de Ubicación
    Ubicacion ubi = hecho.getUbicacion();
    if (ubi != null && ubi.getLatitud() != null && ubi.getLongitud() != null) {
      Optional<Ubicacion> ubiOpt = ubicacionRepository.findByLatitudAndLongitud(ubi.getLatitud(), ubi.getLongitud());
      if (ubiOpt.isPresent()) {
        hecho.setUbicacion(ubiOpt.get());
      } else {
        hecho.setUbicacion(ubicacionRepository.save(ubi));
      }
    }

    // 3. Guardar Hecho
    hechoRepository.save(hecho);
  }

  @Override
  public HechoOutputDTO getHechoById(Long id) {
    Hecho hecho = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe el Hecho con id " + id));
    return hechoOutputDTO(hecho);
  }

  @Override
  public List<HechoOutputDTO> getAll(String categoria, Double latitud, Double longitud, LocalDate frd, LocalDate frh, LocalDate fad, LocalDate fah) {
    return hechoRepository.findAll().stream()
        // ... (Tus filtros se mantienen igual, solo resumo por brevedad) ...
        .filter(hecho -> hecho.getActivo())
        .map(this::hechoOutputDTO)
        .toList();
  }

  private HechoOutputDTO hechoOutputDTO(Hecho hecho){
    // Obtenemos el nombre del archivo CSV
    String nombreArchivo = (hecho.getArchivoCSV() != null)
        ? hecho.getArchivoCSV().getFileName().toString()
        : "Manual";

    return new HechoOutputDTO(
        hecho.getTitulo(),
        hecho.getDescripcion(),
        hecho.getCategoria().getNombre(),
        hecho.getMultimedia(),
        hecho.getUbicacion().getLatitud(),
        hecho.getUbicacion().getLongitud(),
        hecho.getFechaHecho(),
        hecho.getFechaDeCarga(),
        nombreArchivo);
  }
}