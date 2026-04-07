package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.IAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Absoluta implements IAlgoritmoConsenso {
  @Override
  public boolean esConsensuado(Integer cantidadMenciones, Integer totalFuentes) {
    return Objects.equals(cantidadMenciones, totalFuentes);
  }

  @Override
  public TipoAlgoritmoConsenso getTipo() {
    return TipoAlgoritmoConsenso.ABSOLUTA;
  }
}