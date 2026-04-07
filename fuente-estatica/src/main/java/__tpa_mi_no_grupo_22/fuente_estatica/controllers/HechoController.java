package __tpa_mi_no_grupo_22.fuente_estatica.controllers;

import __tpa_mi_no_grupo_22.fuente_estatica.models.dtos.HechoOutputDTO;
import __tpa_mi_no_grupo_22.fuente_estatica.services.IHechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart; // O RequestParam
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
@RestController()
@RequestMapping("/fuente-estatica/hechos")
public class HechoController {
  @Autowired
  private IHechoService hechoService;

  @GetMapping
  public ResponseEntity<List<HechoOutputDTO>> buscarTodosLosHechos(
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) Double latitud,
      @RequestParam(required = false) Double longitud,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_reporte_desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_reporte_hasta,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_acontecimiento_desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_acontecimiento_hasta
  ) {
    return ResponseEntity.ok(this.hechoService.getAll(categoria, latitud, longitud, fecha_reporte_desde, fecha_reporte_hasta, fecha_acontecimiento_desde, fecha_acontecimiento_hasta));
  }

  @GetMapping("/{id}")
  public ResponseEntity<HechoOutputDTO>  buscarHechoPorId(@PathVariable Long id){
    return ResponseEntity.ok(hechoService.getHechoById(id));
  }

  @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> importarHechos(@RequestPart("file") MultipartFile file) {
    try {
      System.out.println(">>> [Fuente Estática] Recibiendo archivo: " + file.getOriginalFilename());

      File tempFile = File.createTempFile("upload_", ".csv");
      file.transferTo(tempFile);

      // Llamamos al servicio (ahora devuelve String inmediatamente)
      // El procesamiento pesado se hace en un hilo secundario dentro del servicio
      hechoService.bringIn(tempFile.toPath());

      return ResponseEntity.ok("Archivo recibido. Lo estaremos procesando.");

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Error al recibir archivo: " + e.getMessage());
    }
  }

  @DeleteMapping("/")
  public ResponseEntity<Void> eliminarHecho(@RequestParam Long id){
    System.out.println("Buscando por id: " + id);
    return ResponseEntity.noContent().build(); // 204 No Content

  }
}
