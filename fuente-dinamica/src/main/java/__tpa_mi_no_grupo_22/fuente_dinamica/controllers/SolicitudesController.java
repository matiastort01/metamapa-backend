package __tpa_mi_no_grupo_22.fuente_dinamica.controllers;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.SoliElimInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.SoliElimOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import __tpa_mi_no_grupo_22.fuente_dinamica.services.solicitudService.ISolicitudService;

import java.util.List;

@RestController
@RequestMapping("/fuente-dinamica/solicitudes")
public class SolicitudesController {
  @Autowired
  private ISolicitudService solicitudService;

  @GetMapping
  public List<SoliElimOutputDTO> buscarTodasLasSolicitudes(){
    return solicitudService.getSolicitudes();
  }

  @GetMapping("/{id}")
  public SoliElimOutputDTO buscarSoliPorId(@PathVariable Long id){
    return solicitudService.getById(id);
  }

  @PostMapping
  public void crearSoli(@RequestBody SoliElimInputDTO soliElimInputDTO) throws Exception {
    solicitudService.save(soliElimInputDTO);
  }

  @DeleteMapping("/")
  public void eliminarSoli(@RequestParam Long id){
    solicitudService.delete(id);
  }
}
