package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.FuenteOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metamapa/fuentes")
public class FuentesController {

  @Autowired
  private IFuentesService fuentesService;

  @GetMapping
  public ResponseEntity<List<FuenteOutputDTO>> getFuentes() {
    return ResponseEntity.ok(fuentesService.getFuentes());
  }
}
