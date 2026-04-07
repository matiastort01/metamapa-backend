package __tpa_mi_no_grupo_22.fuente_proxy.controllers;

import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input.DisasterInputDTO;
import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.output.DisasterOutputDTO;
import __tpa_mi_no_grupo_22.fuente_proxy.services.impl.DesastresService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/desastres")
public class DesastresController {
  private final DesastresService disastersService;

  public DesastresController(DesastresService disastersService) {
    this.disastersService = disastersService;
  }

  @GetMapping
  public Mono<List<DisasterOutputDTO>> getAllDisasters() {
    return disastersService.getAllDisasters();
  }

  @GetMapping("/{id}") //TODO acomodar output
  public Mono<DisasterInputDTO> getDisasterById(@PathVariable Long id) {
    return disastersService.getDisasterById(id);
  }
}