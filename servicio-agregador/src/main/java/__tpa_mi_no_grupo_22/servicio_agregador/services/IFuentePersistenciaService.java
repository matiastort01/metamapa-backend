package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;

public interface IFuentePersistenciaService {
  Fuente findOrCreate(String nombreFuente);
}
