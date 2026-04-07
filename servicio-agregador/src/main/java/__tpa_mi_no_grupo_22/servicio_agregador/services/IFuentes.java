package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuentes {
  public Mono<List<Hecho>> getHechos();

  public TipoFuente getTipo();
}
