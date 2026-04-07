package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IAlgoritmoConsenso {
  public boolean esConsensuado(Integer cantidadMenciones, Integer totalFuentes);

  public TipoAlgoritmoConsenso getTipo();
}
