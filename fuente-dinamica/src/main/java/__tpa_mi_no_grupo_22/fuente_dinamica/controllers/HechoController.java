package __tpa_mi_no_grupo_22.fuente_dinamica.controllers;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.HechoOutputDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import __tpa_mi_no_grupo_22.fuente_dinamica.services.hechoService.IHechoService;
import __tpa_mi_no_grupo_22.fuente_dinamica.services.seederService.ISeederService;
import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/fuente-dinamica/hechos")
public class HechoController {
  @Autowired
  private IHechoService hechoService;
  @Autowired
  private ISeederService seederService;

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

  // TODO: SOLO DEBERIA CREAR, LA MODIFICACION LA HACEMOS EN AGREGADOR
  @PostMapping
  public ResponseEntity<HechoOutputDTO> crearHecho(@RequestBody @Valid HechoInputDTO hechoInputDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.hechoService.crearHecho(hechoInputDTO));
  }

//  @GetMapping("/{id}")
//  public ResponseEntity<HechoOutputDTO> buscarHechoPorId(@PathVariable Long id) {
//    return ResponseEntity.ok(hechoService.getHechoById(id));
//  }

  @GetMapping("/inicializar")
  public ResponseEntity<Boolean> inicializar(){
    this.seederService.init();
    return ResponseEntity.ok(true); // 200 OK + body = true
  }

//  @DeleteMapping("/")
//  public ResponseEntity<Void> eliminarHecho(@RequestParam Long id){
//    System.out.println("Buscando por id: " + id);
//    hechoService.delete(id);
//    return ResponseEntity.noContent().build(); // 204 No Content
//  }
}
