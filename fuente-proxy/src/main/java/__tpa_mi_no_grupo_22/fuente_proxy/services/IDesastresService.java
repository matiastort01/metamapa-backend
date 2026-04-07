package __tpa_mi_no_grupo_22.fuente_proxy.services;

import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input.DisasterInputDTO;
import java.util.List;

import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.output.DisasterOutputDTO;
import reactor.core.publisher.Mono;

public interface IDesastresService {
  Mono<List<DisasterOutputDTO>> getAllDisasters();

  Mono<DisasterInputDTO> getDisasterById(Long id);
}
